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

  implicit lazy val arbInteger: Arbitrary[Int] = Arbitrary(Gen.chooseNum(-10, 10))

  property("Addition works on numbers") {
    forAll { (lhs: Int, rhs: Int) =>
      Number(lhs) + Number(rhs) must be (Number(lhs + rhs))
    }
  }

  property("Addition is commutative") {
    forAll { (lhs:Expression, rhs: Expression) =>
      (lhs + rhs).simplify must be((rhs + lhs).simplify)
    }
  }

  property("Addition is commutative according to monte carlo") {
    forAll { (lhs:Expression, rhs: Expression) =>
      (lhs + rhs).monteCarloEquals(rhs + lhs) must be(true)
    }
  }

  property("Addition is associative") {
    forAll { (a: Expression, b: Expression, c: Expression) =>
      ((a + b) + c).simplify must be(a + (b + c))
    }
  }

  property("Addition is associative according to monte carlo") {
    forAll { (a:Expression, b: Expression, c: Expression) =>
      ((a + b) + c).monteCarloEquals(a + (b + c)) must be(true)
    }
  }

  property("Simplification only needs to be done once") {
    forAll { (exp:Expression) =>
      exp.simplify must be(exp.simplify.simplify)
    }
  }
}
