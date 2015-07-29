package cas

class CasBinaryOperator[A](val name: Name,
                           val properties: List[OperatorProperty],
                           val identities: List[A] = List(),
                           val annihilator: Option[A] = None) {
  def apply(lhs: MathExp[A], rhs: MathExp[A]): MathExp[A] = {
    (is(Commutative), is(Associative), is(Idempotent)) match {
      case (true, true, true) => SetApplication[A](this, Set(lhs, rhs))
      case (false, true, false) => ListApplication[A](this, List(lhs, rhs))
      case _ => BinaryTreeApplication(this, lhs, rhs)
  }}

  def is(props: OperatorProperty*): Boolean = props.forall(properties contains _)

  def isIdentity(a: A) = identities contains a

}

sealed abstract class OperatorProperty
case object Commutative extends OperatorProperty
case object Associative extends OperatorProperty
case object Idempotent extends OperatorProperty
case object Invertible extends OperatorProperty

case class min[A]() extends CasBinaryOperator[A](Name("min"), List(Commutative, Associative, Idempotent))
//object max extends CasBinaryOperator(Name("min"), List(Commutative[Any], Associative[Any], Idempotent[Any]))
