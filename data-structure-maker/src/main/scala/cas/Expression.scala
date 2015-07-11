package cas

sealed abstract class Expression {
  def simplify: Expression = this
  def variables: List[Name]

  def substitute(name: Name, expression: Expression): Expression

  def +(other: Expression): Expression = (this, other) match {
    case (me: Sum, _) => me.addExpr(other)
    case (_, them: Sum) => them.addExpr(this)
    case _ => Sum(Map()).addExpr(this).addExpr(other)
  }

  def *(other: Expression): Expression = Product(Set(this, other))
}

trait NotANumber extends Expression

case class Sum(things: Map[NotANumber, Number], constant: Number = Number(0)) extends Expression with NotANumber {
  override def toString = {
    val constantString = if (constant.value == 0) "" else s" + $constant"
    "(" ++ things.toList.map((things) => {
      if (things._2 == Number(1))
        things._1.toString
      else
        s"${things._2} * ${things._1}"
    }).reduce(_ ++ " + " ++ _) ++ constantString ++ ")"
  }

  lazy val variables = things.keys.flatMap(_.variables).toList

  def substitute(name: Name, expression: Expression) = Sum.build(
    things.map((tuple) => tuple._1.substitute(name, expression) -> tuple._2), constant)

  def addExpr(expr: Expression): Sum = {
    expr match {
      case number: Number => this.copy(constant = this.constant + number)
      case nan: NotANumber => {
        val (coefficient, nonNumericExpr) = expr match {
          case Product(stuff) => {
            val partition = stuff.partition(_.isInstanceOf[Number])
            (Number(partition._1.map(_.asInstanceOf[Number].value).product), Product(partition._2))
          }
          case nan: NotANumber => (Number(1), nan)
        }

        this.things.get(nan) match {
          case Some(currentCoefficient) =>
            Sum(this.things - nan ++ Map(nan -> (coefficient + currentCoefficient)), this.constant)
          case None => Sum(this.things ++ Map(nonNumericExpr -> coefficient), this.constant)
        }
      }
    }
  }

//  def simplify = {
//
//
//  }
}

object Sum {
  def build(things: Map[Expression, Number], constant: Number): Expression = {
    val (newThings, newConstant) = things.map((tuple) => {
      tuple._1 match {
        case Number(value) => (List(), value)
        case exp: NotANumber => (List(exp -> tuple._2), 0)
        case _ => ???
      }
    }).reduce((tuple1, tuple2) => (tuple1._1 ++ tuple2._1, tuple1._2 + tuple2._2))

    Sum(newThings.toMap, Number(newConstant))
  }

  def fromList(things: List[Expression]): Expression = {
    things.reduce(_ + _)
  }
}

case class Product(things: Set[Expression]) extends Expression with NotANumber {
  override def toString = {
    things.map((exp) => {exp.toString}).reduce(_ ++ " * " ++ _)
  }

  lazy val variables = things.flatMap(_.variables).toList

  def substitute(name: Name, expression: Expression) = Product(things.map(_.substitute(name, expression)))
}

case class VariableExpression(name: Name) extends Expression with NotANumber {
  override def toString = name.name

  val variables = List(name)

  def substitute(otherName: Name, expression: Expression) = if (otherName == name) expression else this
}

case class Number(value: Int) extends Expression {
  override def toString = value.toString
  val variables = List()

  def +(other: Number) = Number(this.value + other.value)
  def substitute(name: Name, expression: Expression) = this
}

class DummyVariableExpression extends Expression with NotANumber {
  override def toString = "dummy"
  val variables = List()

  def substitute(name: Name, expression: Expression) = this
}

object Tester {
  implicit def intToNumber(value: Int): Number = Number(value)
  implicit def stringToVariableExpression(name: String): VariableExpression = VariableExpression(Name(name))


  def main (args: Array[String]) {
    val x = VariableExpression(Name("x"))
    val y = VariableExpression(Name("y"))

    println(x + 1 + y * 2 + y + x + 3 + 5)
  }
}
