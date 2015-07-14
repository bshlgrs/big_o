package cas

case class CasFunction(params: List[Name], body: Expression) {
  override def toString = s"f(${params.mkString(", ")}) = $body"
  lazy val isWellFormed = body.variables.forall(params.contains(_))

  def apply(arguments: List[Expression]) = {
    body.substitute(params.zip(arguments).toMap)
  }

  def isCommutative() = {
    val (x, y) = (new DummyVariableExpression, new DummyVariableExpression)
    body.substitute(params.zip(List(x, y)).toMap).simplify == body.substitute(params.zip(List(y, x)).toMap).simplify
  }
}

object ExampleFunctions {
  implicit def intToNumber(value: Int): Number = Number(value)
  implicit def stringToName(name: String): Name = Name(name)
  implicit def nameToVariableExpression(name: Name): VariableExpression = VariableExpression(name)

  val plus = CasFunction(List("x", "y"), VariableExpression("x") + VariableExpression("y"))
  val plus2 = CasFunction(List("x", "y"), VariableExpression("x") + VariableExpression("y") * 2)

  def main (args: Array[String]) {
    println(plus)
    println(plus.isCommutative())
    println(plus2)
    println(plus2.isCommutative())
  }
}
