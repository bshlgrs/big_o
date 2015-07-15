package cas

import scala.util.Random

sealed abstract class Expression {
  lazy val simplify: Expression = this
  def variables: Set[Name]

  def substitute(names: Map[Name, Expression]): Expression

  def separateOutCoefficient(): (Number, Option[NotANumber]) = this match {
    case x: NotANumber => (Number(1), Some(x))
    case x: Number => (x, None)
  }

  val base = this
  lazy val exponent: Expression = Number(1)

  def desumify = this
  def deproductify = this

  def +(other: Expression): Expression = (this, other) match {
    case (me: Sum, _) => me.addExpr(other)
    case (_, them: Sum) => them.addExpr(this)
    case _ => Sum.oneItemSum(this) + other
  }

  def *(other: Expression): Expression = (this, other) match {
    case (me: Product, _) => me.addFactor(other)
    case (_, them: Product) => them.addFactor(this)
    case _ => Product(Set(this)).addFactor(other)
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

trait NotANumber extends Expression

case class Sum(things: Map[Option[NotANumber], Number]) extends Expression with NotANumber {
  val constant = things.getOrElse(None, Number(0))

  val nonConstantTerms: Map[NotANumber, Number] = things.filter(_._1 != None).map((x) => x._1.get -> x._2)

  override lazy val simplify = {
    Sum.fromList(explicitTerms)
  }

  lazy val explicitTerms: List[Expression] = things.toList.map(tuple => tuple._1.getOrElse(Number(1)) * tuple._2)

  lazy val variables: Set[Name] = things.keys.flatMap((x) => if (x == None) Nil else x.get.variables).toSet

  def substitute(map: Map[Name, Expression]) = {
    Sum.fromList(nonConstantTerms.toList.map((tuple) => tuple._1.substitute(map) * tuple._2)) + constant
  }

  def addExpr(expr: Expression): Expression = {
    expr match {
      case Sum(insideThings) => insideThings.foldLeft(this: Expression) { (currentValue, newTuple ) =>
        currentValue + newTuple._1.getOrElse(Number(1)) * newTuple._2
      }
      case _ => {
        val (coefficient, nonNumericExpr) = expr.separateOutCoefficient()
        val newCoefficient = things.getOrElse(nonNumericExpr, Number(0)) + coefficient
        val newMap = if (newCoefficient.value == 0) Map() else Map(nonNumericExpr -> newCoefficient)
        Sum(this.things - nonNumericExpr ++ newMap)
      }
    }
  }

  override def desumify(): Expression = {
    this.things.size match {
      case 0 => Number(0)
      case 1 => this.things.toList.head match {
        case (None, x) => x
        case (Some(x), y) => Product(Set(x, y))
      }
      case 2 => this
    }
  }
}

object Sum {
  def fromList(things: List[Expression]): Expression = {
    things.length match {
      case 0 => Number(0)
      case 1 => things.head
      case _ => things.reduce(_ + _)
    }
  }

  def oneItemSum(thing: Expression) = {
    val (coefficient, nonNumericExpr) = thing.separateOutCoefficient()
    Sum(Map(nonNumericExpr -> coefficient))
  }
}

case class Product(terms: Set[Expression]) extends Expression with NotANumber {
//  override def toString = {
//    terms.map((exp) => {exp.toString}).reduce(_ ++ " * " ++ _)
//  }

  override def deproductify() = terms.size match {
    case 0 => Number(1)
    case 1 => terms.toList.head
    case n => this
  }

  lazy val variables = terms.flatMap(_.variables)

  def substitute(map: Map[Name, Expression]) = terms.toList.map(_.substitute(map)).reduce(_ * _)

  override lazy val simplify = terms.size match {
    case 0 => Number(1)
    case 1 => terms.toList.head
    case n => this
  }

  override def separateOutCoefficient() = {
    val partition = terms.partition(_.isInstanceOf[Number])

    val coef = Number(partition._1.map(_.asInstanceOf[Number].value).product)

    partition._2.size match {
      case 0 => (coef, None)
      case _ => (coef, Some(Product(partition._2)))
    }
  }

  def getPowerForBase(base: Expression): Expression = {
    val relevantTerms = terms.filter(_.base == base)
    relevantTerms.size match {
      case 0 => Number(0)
      case 1 => relevantTerms.toList.head
      case _ => throw new RuntimeException("invariant violated")
    }
  }

  def addFactor(factor: Expression): Expression = {
    val oldExponent = getPowerForBase(factor.base)
    val newExponent = (oldExponent + factor.exponent).desumify

    val otherFactors = terms.filter(_.base != factor.base)

    (newExponent match {
      case Number(0) => Product(otherFactors)
      case Number(1) => Product(otherFactors + factor.base)
      case _ => Product(otherFactors + Power(factor.base, newExponent))
    }).deproductify()
  }
}

case class Power(_base: Expression, _exponent: Expression) extends Expression with NotANumber {
  override val base = _base
  override lazy val exponent = _exponent

  val variables = base.variables ++ exponent.variables

  def substitute(map: Map[Name, Expression]) = {
    Power(base.substitute(map), exponent.substitute(map))
  }

  override lazy val simplify = {
    if (exponent.simplify == Number(1))
      base
    else
      Power(base.simplify, exponent.simplify)
  }
}

object Power {
  def build(base: Expression, exponent: Expression) = {
    Power(base, exponent).simplify
  }
}

case class VariableExpression(name: Name) extends Expression with NotANumber {
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

class DummyVariableExpression extends Expression with NotANumber {
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


    val expr = y + 0
    val expr2= Sum(Map(Some(Power(x,Sum(Map(None -> 1)))) -> 4, Some(Power(y,Sum(Map(None -> 1)))) -> 3))
    println(expr)
//    println(expr.substitute(Map(Name("y") -> Number(3))))


  }
}
