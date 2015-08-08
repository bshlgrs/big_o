package java_transpiler.queries

import java_transpiler.{JavaExpressionOrQuery, JavaExpression}

import cas.MathExp

abstract class Reduction

case class Mapper(arg: String, body: JavaExpressionOrQuery)
case class Reducer(arg1: String, arg2: String, body: JavaExpressionOrQuery)

case object SelectStar extends Reduction
case class FoldReduction(start: JavaExpressionOrQuery,
                         mapper: Mapper,
                         reducer: Reducer) extends Reduction

