package queries

import cas.MathExp

sealed abstract class WhereClause

case class ConstantWhereClause(nodeFunction: MathExp[FML])

case class ParameterEqualityWhereClause(parameterFunction: MathExp[FML], nodeFunction: MathExp[FML])

case class ParameterGreaterThanWhereClause(parameterFunction: MathExp[FML],
                                           nodeFunction: MathExp[FML],
                                           IncludesEqual: Boolean)
