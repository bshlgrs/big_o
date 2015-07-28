package java_transpiler.queries

import java_transpiler.{JavaExpressionOrQuery, JavaExpression}

import cas.MathExp

class Reduction {


}

case object SelectStar extends Reduction
case class FoldReduction(start: MathExp[JavaExpressionOrQuery], mapper: MathExp[JavaExpressionOrQuery], reducer: MathExp[JavaExpressionOrQuery]) extends Reduction
