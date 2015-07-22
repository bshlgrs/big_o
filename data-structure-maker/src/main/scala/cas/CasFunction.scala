package cas

case class CasFunction[A](params: List[A], body: MathExp[A]) {
  override def toString = s"f(${params.mkString(", ")}) = $body"
  lazy val isWellFormed = body.variables.forall(params.contains(_))

  def apply(arguments: MathExp[A] *) = {
    assert(arguments.length == params.length)
    body.substitute(params.zip(arguments).toMap).simplify
  }

  def isCommutative() = {
    val (x, y) = (new DummyVariableMathExp[A], new DummyVariableMathExp[A])
    apply(x, y) == apply(y, x)
  }

  def isAssociative() = {
    val (x, y, z) = (new DummyVariableMathExp[A], new DummyVariableMathExp[A], new DummyVariableMathExp[A])
    apply(x, apply(y, z)) == apply(apply(x, y), z)
  }
}

object ExampleFunctions {
  implicit def intToNumber(value: Int): Number[_] = Number(value)
  implicit def stringToName(name: String): Name = Name(name)
  implicit def nameToVariableExpression(name: Name): VariableMathExp[_] = VariableMathExp(name)

  val plus = CasFunction(List("x", "y"), VariableMathExp("x") + VariableMathExp("y"))
  val plus2 = CasFunction(List("x", "y"), VariableMathExp("x") + VariableMathExp("y") * Number(2))

  def main(args: Array[String]) {
    println(plus)
    println(plus.isCommutative())
    println(plus2)
    println(plus2.isCommutative())
  }
}
