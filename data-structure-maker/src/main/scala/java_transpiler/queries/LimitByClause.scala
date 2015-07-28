package java_transpiler.queries

import java_transpiler.JavaExpressionOrQuery

import cas.MathExp

sealed abstract class LimitByClause

case class ConstantSizeConstantLimitByClause(nodeExprToMaximise: MathExp[JavaExpressionOrQuery], number: Int)
  extends LimitByClause
case class VariableSizeConstantLimitByClause(
                    nodeExprToMaximise: MathExp[JavaExpressionOrQuery],
                    number: MathExp[JavaExpressionOrQuery]) extends LimitByClause
case class RandomShitLimitByClause(paramAndNodeExprToMaximise: MathExp[JavaExpressionOrQuery],
                                   number: MathExp[JavaExpressionOrQuery]) extends LimitByClause
