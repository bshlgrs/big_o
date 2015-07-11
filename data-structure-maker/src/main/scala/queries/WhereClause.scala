package queries

import cas.Expression

sealed abstract class WhereClause

case class ConstantWhereClause(nodeFunction: Expression)

case class ParameterEqualityWhereClause(parameterFunction: Expression, nodeFunction: Expression)

case class ParameterGreaterThanWhereClause(parameterFunction: Expression,
                                           nodeFunction: Expression,
                                           IncludesEqual: Boolean)
