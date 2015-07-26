package queries

import cas.MathExp

sealed abstract class LimitByClause

case class ConstantSizeConstantLimitByClause(nodeExprToMaximise: MathExp[DataSource], number: Int) extends LimitByClause
case class VariableSizeConstantLimitByClause(nodeExprToMaximise: MathExp[DataSource], number: MathExp[DataSource]) extends LimitByClause
case class RandomShitLimitByClause(paramAndNodeExprToMaximise: MathExp[DataSource], number: MathExp[DataSource]) extends LimitByClause
