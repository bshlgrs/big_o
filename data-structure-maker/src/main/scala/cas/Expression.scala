package cas

import scala.util.Random

sealed abstract class Expression {
  lazy val simplify: Expression = this
  def variables: Set[Name]

  def substitute(names: Map[Name, Expression]): Expression

  def +(other: Expression): Expression = (this, other) match {
    case (x: Sum, y: Sum) => x.summands.foldLeft(y: Expression)(_ + _)
    case (x: Sum, _) => Sum.addTerm(x.summands, other)
    case (_, y: Sum) => Sum.addTerm(y.summands, this)
    case (Number(0), x) => x
    case (x, Number(0)) => x
    case (_, _) => Sum.addTerm(Set(this), other)
  }

  def *(other: Expression): Expression = (this, other) match {
    case (x: Product, y: Product) => x.terms.foldLeft(y: Expression)(_ * _)
    case (x: Product, _) => Product.addFactor(x.terms, other)
    case (_, y: Product) => Product.addFactor(y.terms, this)
    case (_, _) => Product.addFactor(Set(this), other)
  }

  def monteCarloEquals(other: Expression): Boolean = {
    if (this.variables != other.variables) {
      false
    } else {
      val r = new Random
      val values = this.variables.map(_ -> Number(r.nextInt(9999))).toMap

      this.substitute(values) == other.substitute(values)
    }
  }
}

case class Sum(summands: Set[Expression]) extends Expression {
  assert(summands.size > 1, s"summands are $summands")
  assert(summands.count(_.isInstanceOf[Number]) < 2, s"summands are $summands")
  assert(summands.find(_ == Number(0)) == None, s"summands are $summands")

  lazy val variables: Set[Name] = summands.flatMap(_.variables)

  def substitute(map: Map[Name, Expression]) = {
    summands.toList.map(_.substitute(map)).reduce(_ + _)
  }
}

object Sum {
  import ExpressionHelper._

  def addTerm(summands: Set[Expression], newSummand: Expression): Expression = {
    assert(!newSummand.isInstanceOf[Sum])

    val (newSummandConstantTerm, mbNewSummandExpression) = splitCoefficient(newSummand)

    if (newSummandConstantTerm.value == 0)
      buildFromValidSummandsSet(summands)
    else {
      val (otherItems, mbRelatedItem) = grabByPredicate(summands, { (x: Expression) =>
        splitCoefficient(x)._2 == mbNewSummandExpression })

      mbRelatedItem match {
        case None => buildFromValidSummandsSet(otherItems ++ Set(newSummand))
        case Some(item) => {
          val newCoefficientValue = newSummandConstantTerm.value + splitCoefficient(item)._1.value
          if (newCoefficientValue == 0)
            buildFromValidSummandsSet(otherItems)
          else {
            val thingToAdd = mbNewSummandExpression.map(_ * Number(newCoefficientValue)).getOrElse(Number(newCoefficientValue))
            buildFromValidSummandsSet(otherItems ++ Set(thingToAdd))
          }
        }
      }
    }
  }

  def buildFromValidSummandsSet(summands: Set[Expression]): Expression = {
    summands.size match {
      case 0 => Number(0)
      case 1 => summands.head
      case _ => Sum(summands)
    }
  }
}

case class Product(terms: Set[Expression]) extends Expression {
  assert(terms.size > 1, s"terms are $terms")
  assert(terms.count(_.isInstanceOf[Number]) <= 1, s"terms are $terms")

  lazy val variables = terms.flatMap(_.variables)

  def substitute(map: Map[Name, Expression]) = terms.toList.map(_.substitute(map)).reduce(_ * _)
}

object Product {
  def addFactor(terms: Set[Expression], newTerm: Expression): Expression = {
    Product.buildFromValidTermsSet(terms ++ Set(newTerm))
  }

  def buildFromValidTermsSet(terms: Set[Expression]): Expression = {
    terms.size match {
      case 0 => Number(1)
      case 1 => terms.head
      case _ => Product(terms)
    }
  }
}

case class Power(base: Expression, exponent: Expression) extends Expression {
  val variables = base.variables ++ exponent.variables

  assert(base != Number(1))
  assert(exponent != Number(1))

  def substitute(map: Map[Name, Expression]) = {
    Power(base.substitute(map), exponent.substitute(map))
  }
}

case class VariableExpression(name: Name) extends Expression {
  override def toString = name.name

  val variables = Set(name)

  def substitute(map: Map[Name, Expression]) = if (map.contains(name)) map(name) else this
}

case class Number(value: Int) extends Expression {
  override def toString = value.toString
  val variables = Set[Name]()

  def +(other: Number) = Number(this.value + other.value)
  def *(other: Number) = Number(this.value * other.value)
  def **(other: Number) = Number(Math.pow(this.value, other.value).toInt)
  def substitute(map: Map[Name, Expression]) = this
}

class DummyVariableExpression extends Expression {
  override def toString = "dummy"
  val variables = Set[Name]()

  def substitute(map: Map[Name, Expression]) = this
}

object Tester {
  implicit def intToNumber(value: Int): Number = Number(value)
  implicit def stringToName(name: String): Name = Name(name)
  implicit def nameToVariableExpression(name: Name): VariableExpression = VariableExpression(name)

  def main (args: Array[String]) {
    val x = VariableExpression(Name("x"))
    val y = VariableExpression(Name("y"))


    val expr = y + 0 + 0
    println(expr)
  }
}

object ExpressionHelper {
  def grabByPredicate[A](set: Set[A], predicate: A => Boolean): (Set[A], Option[A]) = {
    val (specialThings, remainingItems) = set.partition(predicate)
    assert(specialThings.size <= 1)
    (remainingItems, specialThings.headOption)
  }

  def splitCoefficient(exp: Expression): (Number, Option[Expression]) = {
    exp match {
      case n : Number => (n, None)
      case p : Product => {
        val number = p.terms.find(_.isInstanceOf[Number]).getOrElse(Number(1)).asInstanceOf[Number]
        val exp = Product.buildFromValidTermsSet(p.terms.filterNot(_.isInstanceOf[Number]))
        (number, Some(exp))
      }
      case _ => (Number(1), Some(exp))
    }
  }
}
