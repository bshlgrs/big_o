package queries

import cas.MathExp

sealed abstract class WhereClause

case class ConstantWhereClause(nodeFunction: MathExp[DataSource])

case class ParameterEqualityWhereClause(parameterFunction: MathExp[DataSource], nodeFunction: MathExp[DataSource])

case class ParameterGreaterThanWhereClause(parameterFunction: MathExp[DataSource],
                                           nodeFunction: MathExp[DataSource],
                                           IncludesEqual: Boolean)
