package cas

import scala.util.Random

sealed abstract class MathExp[A] {
  type NameExpression = MathExp[Name]

  lazy val simplify: MathExp[A] = this
  def variables: Set[A]

  def substitute(names: Map[A, MathExp[A]]): MathExp[A]

  def +(other: MathExp[A]): MathExp[A] = (this, other) match {
    case (x: Sum[A], y: Sum[A]) => x.summands.foldLeft(y: MathExp[A])(_ + _)
    case (x: Sum[A], _) => Sum.addTerm(x.summands, other)
    case (_, y: Sum[A]) => Sum.addTerm(y.summands, this)
    case (Number(0), x) => x
    case (x, Number(0)) => x
    case (_, _) => Sum.addTerm(Set(this), other)
  }

  def *(other: MathExp[A]): MathExp[A] = (this, other) match {
    case (x: Product[A], y: Product[A]) => x.terms.foldLeft(y: MathExp[A])(_ * _)
    case (x: Product[A], _) => Product.addFactor(x.terms, other)
    case (_, y: Product[A]) => Product.addFactor(y.terms, this)
    case (Number(0), x) => Number(0)
    case (x, Number(0)) => Number(0)
    case (Number(1), x) => x
    case (x, Number(1)) => x
    case (_, _) => Product.addFactor(Set(this), other)
  }

  def -(other: MathExp[A]): MathExp[A] = this + other * Number(-1)

  def /(other: MathExp[A]): MathExp[A] = this * other ** Number(-1)

  def **(exponent: MathExp[A]): MathExp[A] = (this, exponent) match {
    case (_, Number(0)) => Number(1)
    case (Number(0), _) => Number(0)
    case (Number(1), _) => Number(1)
    case (_, Number(1)) => this
    case (Number(x), Number(y)) => (Number(x) ** Number(y)).asInstanceOf[MathExp[A]]
    case (Power(x, y), other) => x ** (y * other)
    case (x: Product[A], _) => x.terms.map(_ ** exponent).foldLeft(Number(1): MathExp[A])(_ * _)
    case (_, _) => Power(this, exponent)
  }

  def monteCarloEquals(other: MathExp[A]): Boolean = {
    if (this.variables != other.variables) {
      false
    } else {
      val r = new Random
      val values: Map[A, Number[A]] = this.variables.map(_ -> Number[A](r.nextInt(9999))).toMap

      this.substitute(values) == other.substitute(values)
    }
  }

  def applyBinaryOperator(op: CasBinaryOperator[A], other: MathExp[A]) = ??? // this match {

//  }

//  def solve(name: A, otherSide: Expression): Option[List[Expression]]
}

case class Sum[A] private (summands: Set[MathExp[A]]) extends MathExp[A] {
  assert(summands.size > 1, s"summands are $summands")
  assert(summands.count(_.isInstanceOf[Number[A]]) <= 1, s"summands are $summands")
  assert(summands.find(_ == Number(0)) == None, s"summands are $summands")

  lazy val variables: Set[A] = summands.flatMap(_.variables)

  def substitute(map: Map[A, MathExp[A]]): MathExp[A] = {
    summands.toList.map(_.substitute(map)).reduce((x: MathExp[A], y: MathExp[A]) => x + y)
  }

//  def solve(name: A, otherSide: Expression) = {
//    val (doesNotContainA, containsA) = summands.partition(_.variables.contains(name))
//
//    containsName.size match {
//      case 0 =>
//
//    }
//  }
}

object Sum {
  import ExpressionHelper._

  def addTerm[A](summands: Set[MathExp[A]], newSummand: MathExp[A]): MathExp[A] = {
    assert(!newSummand.isInstanceOf[Sum[_]])

    val (newSummandConstantTerm, mbNewSummandExpression) = splitCoefficient(newSummand)

    if (newSummandConstantTerm.value == 0)
      buildFromValidSummandsSet(summands)
    else {
      val (otherItems, mbRelatedItem) = grabByPredicate(summands, { (x: MathExp[A]) =>
        splitCoefficient(x)._2 == mbNewSummandExpression })

      mbRelatedItem match {
        case None => buildFromValidSummandsSet(otherItems ++ Set(newSummand))
        case Some(item) => {
          val newCoefficientValue = newSummandConstantTerm.value + splitCoefficient(item)._1.value
          if (newCoefficientValue == 0)
            buildFromValidSummandsSet(otherItems)
          else {
            val thingToAdd: MathExp[A] = mbNewSummandExpression.map(_ * Number(newCoefficientValue))
              .getOrElse(Number(newCoefficientValue))
            buildFromValidSummandsSet(otherItems ++ Set(thingToAdd))
    }}}}
  }

  private def buildFromValidSummandsSet[A](summands: Set[MathExp[A]]): MathExp[A] = {
    summands.size match {
      case 0 => Number(0)
      case 1 => summands.head
      case _ => Sum(summands).asInstanceOf[MathExp[A]] // todo: why do I need this?
    }
  }
}

case class Product[A] private (terms: Set[MathExp[A]]) extends MathExp[A] {
  assert(terms.size > 1, s"terms are $terms")
  assert(terms.count(_.isInstanceOf[Number[A]]) <= 1, s"terms are $terms")

  lazy val variables = terms.flatMap(_.variables)

  def substitute(map: Map[A, MathExp[A]]) = terms.toList.map(_.substitute(map)).reduce(_ * _)
}

object Product {
  import ExpressionHelper._

  def addFactor[A](terms: Set[MathExp[A]], newTerm: MathExp[A]): MathExp[A] = {
    newTerm match {
      case n: Number[A] => addNumericFactor(terms, n)
      case _ => addNonNumericFactor(terms, newTerm)
    }
  }

  private def addNumericFactor[A](terms: Set[MathExp[A]], newTerm: Number[A]): MathExp[A] = {
    val (nonNumericItems, mbNumericItem) = grabByPredicate(terms, (x: MathExp[A]) => x.isInstanceOf[Number[A]])
    val number = newTerm.value * mbNumericItem.getOrElse(Number(1)).asInstanceOf[Number[A]].value

    number match {
      case 0 => Number(0)
      case 1 => Product.buildFromValidTermsSet(nonNumericItems)
      case n => Product.buildFromValidTermsSet(nonNumericItems ++ Set(Number[A](n)))
    }
  }

  private def addNonNumericFactor[A](terms: Set[MathExp[A]], newTerm: MathExp[A]): MathExp[A] = {
    val (base, exponent) = splitBaseAndExponent(newTerm)

    if (base == Number(1) || exponent == Number(0))
      buildFromValidTermsSet(terms)
    else {
      val (otherItems, mbRelatedItem) = grabByPredicate(terms, { (x: MathExp[A]) =>
        splitBaseAndExponent(x)._1 == base })

      mbRelatedItem match {
        case None => buildFromValidTermsSet(otherItems ++ Set(newTerm))
        case Some(item) => {
          val newExponent = exponent + splitBaseAndExponent(item)._2
          if (newExponent == Number(0))
            buildFromValidTermsSet(otherItems)
          else {
            val termToAdd = base ** newExponent
            buildFromValidTermsSet(otherItems ++ Set(termToAdd))
    }}}}
  }

  def buildFromValidTermsSet[A](terms: Set[MathExp[A]]): MathExp[A] = {
    terms.size match {
      case 0 => Number(1)
      case 1 => terms.head
      case _ => Product(terms)
    }
  }
}

abstract class BinaryOperatorApplication[A](op: CasBinaryOperator[A]) extends MathExp[A] {
  def combineWithCollection(other: this.type): MathExp[A]
  def leftCombineWithItem(other: MathExp[A]): MathExp[A]
  def rightCombineWithItem(other: MathExp[A]): MathExp[A]
  def perhapsDeflate(): MathExp[A]
}

case class SetApplication[A](op: CasBinaryOperator[A], set: Set[MathExp[A]]) extends BinaryOperatorApplication[A](op) {
  assert(op.is(Commutative, Associative, Idempotent))

  lazy val variables = set.flatMap(_.variables)

  def substitute(map: Map[A, MathExp[A]]): MathExp[A] = SetApplication[A](op, set.map(_.substitute(map)))

  def combineWithCollection(other: this.type): MathExp[A] = {
    assert(this.op == other.op)
    SetApplication(op, set ++ other.set).asInstanceOf[MathExp[A]]
  }

  def leftCombineWithItem(item: MathExp[A]) = {
    SetApplication(op, set + item).perhapsDeflate()
  }

  def rightCombineWithItem(item: MathExp[A]) = leftCombineWithItem(item)

  def perhapsDeflate() = this.set.size match {
    case 0 => ???
    case 1 => this.set.toList.head
    case _ => this
  }
}

case class ListApplication[A](op: CasBinaryOperator[A], list: List[MathExp[A]]) extends BinaryOperatorApplication[A](op) {
  lazy val variables = list.flatMap(_.variables).toSet

  def substitute(map: Map[A, MathExp[A]]): MathExp[A] = ListApplication[A](op, list.map(_.substitute(map)))

  def combineWithCollection(other: this.type): MathExp[A] = {
    assert(this.op == other.op)
    ListApplication(op, list ++ other.list).asInstanceOf[MathExp[A]]
  }

  def leftCombineWithItem(item: MathExp[A]) = {
    ListApplication(op, list :+ item).perhapsDeflate()
  }

  def rightCombineWithItem(item: MathExp[A]) = leftCombineWithItem(item)

  def perhapsDeflate() = this.list.size match {
    case 0 => ???
    case 1 => this.list.head
    case _ => this
  }
}

case class BinaryTreeApplication[A](op: CasBinaryOperator[A], lhs: MathExp[A], rhs: MathExp[A]) extends BinaryOperatorApplication[A](op) {
  lazy val variables = lhs.variables ++ rhs.variables

  def substitute(map: Map[A, MathExp[A]]) = op.apply(lhs.substitute(map), rhs.substitute(map))

  def combineWithCollection(other: this.type) = {
    assert(this.op == other.op)
    BinaryTreeApplication(op, this, other)
  }

  def leftCombineWithItem(item: MathExp[A]) = BinaryTreeApplication(op, this, item)
  def rightCombineWithItem(item: MathExp[A]) = BinaryTreeApplication(op, item, this)
  def perhapsDeflate() = this
}


case class Power[A](base: MathExp[A], exponent: MathExp[A]) extends MathExp[A] {
  val variables = base.variables ++ exponent.variables

  assert(base != Number(1))
  assert(exponent != Number(1))

  def substitute(map: Map[A, MathExp[A]]) = {
    base.substitute(map) ** exponent.substitute(map)
  }
}

case class VariableMathExp[A](name: A) extends MathExp[A] {
  override def toString = name.toString

  val variables = Set(name)

  def substitute(map: Map[A, MathExp[A]]) = if (map.contains(name)) map(name) else this
}

case class Number[A](value: Int) extends MathExp[A] {
  override def toString = value.toString
  val variables = Set[A]()

  def +(other: Number[A]) = Number(this.value + other.value)
  def *(other: Number[A]) = Number(this.value * other.value)
  def **(other: Number[A]) = Number(Math.pow(this.value, other.value).toInt)
  def substitute(map: Map[A, MathExp[A]]) = this
}

class DummyVariableMathExp[A] extends MathExp[A] {
  override def toString = "dummy"
  val variables = Set[A]()

  def substitute(map: Map[A, MathExp[A]]) = this
}

object ExpressionHelper {
  def grabByPredicate[A](set: Set[A], predicate: A => Boolean): (Set[A], Option[A]) = {
    val (specialThings, remainingItems) = set.partition(predicate)
    assert(specialThings.size <= 1)
    (remainingItems, specialThings.headOption)
  }

  def splitCoefficient[A](exp: MathExp[A]): (Number[A], Option[MathExp[A]]) = {
    exp match {
      case n : Number[A] => (n, None)
      case p : Product[A] =>
        val number = p.terms.find(_.isInstanceOf[Number[A]]).getOrElse(Number(1)).asInstanceOf[Number[A]]
        val exp = Product.buildFromValidTermsSet(p.terms.filterNot(_.isInstanceOf[Number[A]]))
        (number, Option(exp))
      case _ => (Number(1), Some(exp))
    }
  }

  def splitBaseAndExponent[A](exp: MathExp[A]): (MathExp[A], MathExp[A]) = {
    exp match {
      case Power(base, exponent) => (base, exponent)
      case _ => (exp, Number(1))
    }
  }
}
