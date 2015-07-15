package cas

import org.scalatest.PropSpec
import org.scalatest.prop.PropertyChecks
import org.scalatest.prop.Checkers
import org.scalatest.matchers.MustMatchers
import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalacheck.Prop.forAll

object ExpressionGenerators {
  lazy val genExpression: Gen[Expression] = for {
    variable <- genVariable
    sum <- genSum
    constant <- genConstant
    result <- Gen.oneOf(variable, constant, sum)
  } yield result

  lazy val genVariable = Gen.oneOf("x", "y", "z").map((name: String) => VariableExpression(Name(name)))

  lazy val genSum = Gen.nonEmptyListOf(genVariable).map(Sum.fromList)

  lazy val genConstant = Gen.oneOf(Number(0), Number(1), Number(2))

  implicit lazy val arbExpression: Arbitrary[Expression] = Arbitrary(genExpression)
}

class ExpressionTests extends PropSpec with PropertyChecks with MustMatchers {
  import ExpressionGenerators._

  property("Addition is commutative") {
    forAll { (lhs:Expression, rhs: Expression) =>
      println(s"$lhs, $rhs")
      (lhs + rhs).simplify must be((rhs + lhs).simplify)
    }
  }

  property("Simplification only needs to be done once") {
    forAll { (exp:Expression) =>
      exp.simplify must be(exp.simplify.simplify)
    }
  }
}
