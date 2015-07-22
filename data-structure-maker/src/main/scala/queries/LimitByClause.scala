package queries

import cas.MathExp

sealed abstract class LimitByClause

case class ConstantSizeConstantLimitByClause(nodeExprToMaximise: MathExp[FML], number: Int) extends LimitByClause
case class VariableSizeConstantLimitByClause(nodeExprToMaximise: MathExp[FML], number: MathExp[FML]) extends LimitByClause
case class RandomShitLimitByClause(paramAndNodeExprToMaximise: MathExp[FML], number: MathExp[FML]) extends LimitByClause
