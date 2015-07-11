package queries

import cas.Expression

sealed abstract class LimitByClause

case class ConstantSizeConstantLimitByClause(nodeExprToMaximise: Expression, number: Int) extends LimitByClause
case class VariableSizeConstantLimitByClause(nodeExprToMaximise: Expression, number: Expression) extends LimitByClause
case class RandomShitLimitByClause(paramAndNodeExprToMaximise: Expression, number: Expression) extends LimitByClause
