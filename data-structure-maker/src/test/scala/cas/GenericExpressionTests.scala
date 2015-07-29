package cas

import org.scalatest.PropSpec
import org.scalatest.prop.PropertyChecks
import org.scalatest.prop.Checkers
import org.scalatest.matchers.MustMatchers
import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalacheck.Prop.forAll


class GenericExpressionTests extends PropSpec with PropertyChecks with MustMatchers {

  import ExpressionGenerators._

  implicit lazy val arbInteger: Arbitrary[Int] = Arbitrary(Gen.chooseNum(-10, 10))

  type Exp = MathExp[Name]

  property("Min is commutative") {
    forAll { (a: Exp, b: Exp) =>
      min()(a, b) must be(min()(b, a))
    }
  }

  property("Min is associative") {
    forAll { (a: Exp, b: Exp, c: Exp) =>
      min()(a, min()(b, c)) must be(min()(min()(a, b), c))
    }
  }
}
