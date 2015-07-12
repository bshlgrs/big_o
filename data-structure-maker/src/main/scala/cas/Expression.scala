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
    val constantString: List[String] = if (constant.value == 0) Nil else List(constant.toString)
    val variablesString = things.toList.map((things) => {
      if (things._2 == Number(1))
        things._1.toString
      else
        s"${things._2} * ${things._1}"
    })

    "(" ++ (variablesString ++ constantString).reduceLeftOption(_ ++ " + " ++ _).getOrElse("") ++ ")"
  }

  lazy val variables = things.keys.flatMap(_.variables).toList

  def substitute(name: Name, expression: Expression) = {
    Sum.fromList(things.toList.map((tuple) => tuple._1.substitute(name, expression) * tuple._2)) + constant
  }

  def addExpr(expr: Expression): Sum = {
    println(s"this is $this, expr is $expr")
    expr match {
      case number: Number => this.copy(constant = this.constant + number)
      case nan: NotANumber => {
        val (coefficient, nonNumericExpr) = expr match {
          case Product(stuff) => {
            val partition = stuff.partition(_.isInstanceOf[Number])
            (Number(partition._1.map(_.asInstanceOf[Number].value).product),
              Product(partition._2).simplify.asInstanceOf[NotANumber])
          }
          case nan: NotANumber => (Number(1), nan)
          case Number(_) => ???
        }

        this.things.get(nonNumericExpr) match {
          case Some(currentCoefficient) =>
            Sum(this.things - nonNumericExpr ++ Map(nonNumericExpr -> (coefficient + currentCoefficient)), this.constant)
          case None => Sum(this.things ++ Map(nonNumericExpr -> coefficient), this.constant)
        }
      }
    }
  }
}

object Sum {
  def fromList(things: List[Expression]): Expression = {
    println(s"things are $things")
    val result = things.reduce(_ + _)
    println(s"result is $result")
    result
  }
}

case class Product(things: Set[Expression]) extends Expression with NotANumber {
  override def toString = {
    things.map((exp) => {exp.toString}).reduce(_ ++ " * " ++ _)
  }

  lazy val variables = things.flatMap(_.variables).toList

  def substitute(name: Name, expression: Expression) = Product(things.map(_.substitute(name, expression)))

  override def simplify = things.size match {
    case 0 => Number(1)
    case 1 => things.toList.head
    case n => this
  }
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

    val expr = 4 + 3 * y
    println(expr)
    println(expr.substitute(Name("y"), x))
  }
}
