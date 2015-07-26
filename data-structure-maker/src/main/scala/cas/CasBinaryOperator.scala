package cas

case class CasBinaryOperator[A](name: Name, properties: List[OperatorProperty[A]]) {
  def apply[A](lhs: MathExp[A], rhs: MathExp[A]): MathExp[A] = {
    ???
  }

  def is(props: OperatorProperty[A]*): Boolean = props.forall(properties contains _)
}

sealed abstract class OperatorProperty[A]
case object Commutative extends OperatorProperty
case object Associative extends OperatorProperty
case object Idempotent extends OperatorProperty
case object Invertible extends OperatorProperty
case class Identity[A](identity: A) extends OperatorProperty[A]

//case class min[A]() extends CasBinaryOperator[A](Name("min"), List(Commutative, Associative, Idempotent))
//object max extends CasBinaryOperator(Name("min"), List(Commutative[Any], Associative[Any], Idempotent[Any]))
