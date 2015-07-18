package cas

case class CasFunction(params: List[Name], body: Expression) {
  override def toString = s"f(${params.mkString(", ")}) = $body"
  lazy val isWellFormed = body.variables.forall(params.contains(_))

  def apply(arguments: Expression *) = {
    assert(arguments.length == params.length)
    body.substitute(params.zip(arguments).toMap).simplify
  }

  def isCommutative() = {
    val (x, y) = (new DummyVariableExpression, new DummyVariableExpression)
    apply(x, y) == apply(y, x)
  }

  def isAssociative() = {
    val (x, y, z) = (new DummyVariableExpression, new DummyVariableExpression, new DummyVariableExpression)
    apply(x, apply(y, z)) == apply(apply(x, y), z)
  }
}

object ExampleFunctions {
  implicit def intToNumber(value: Int): Number = Number(value)
  implicit def stringToName(name: String): Name = Name(name)
  implicit def nameToVariableExpression(name: Name): VariableExpression = VariableExpression(name)

  val plus = CasFunction(List("x", "y"), VariableExpression("x") + VariableExpression("y"))
  val plus2 = CasFunction(List("x", "y"), VariableExpression("x") + VariableExpression("y") * 2)

  def main(args: Array[String]) {
    println(plus)
    println(plus.isCommutative())
    println(plus2)
    println(plus2.isCommutative())
  }
}
