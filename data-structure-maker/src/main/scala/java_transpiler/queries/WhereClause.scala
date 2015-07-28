package java_transpiler.queries

import java_transpiler.JavaExpressionOrQuery

import cas.MathExp

sealed abstract class WhereClause

case class ConstantWhereClause(nodeFunction: MathExp[JavaExpressionOrQuery])

case class ParameterEqualityWhereClause(parameterFunction: MathExp[JavaExpressionOrQuery], nodeFunction: MathExp[JavaExpressionOrQuery])

case class ParameterGreaterThanWhereClause(parameterFunction: MathExp[JavaExpressionOrQuery],
                                           nodeFunction: MathExp[JavaExpressionOrQuery],
                                           IncludesEqual: Boolean)
