package java_transpiler.queries

import java_transpiler._

import cas.{Number, Name}

case class UnorderedQuery(
            source: String,
            whereClauses: List[WhereClause],
            limiter: Option[LimitByClause],
            reduction: Option[Reduction]) {

  val parameters = whereClauses.flatMap(_.freeVariables)++ limiter.map(_.freeVariables) ++ reduction.map(_.freeVariables)

  def childrenExpressions(): List[JavaExpressionOrQuery] = {
    whereClauses.flatMap(_.childrenExpressions())
  }

  def applyMethod(methodName: String, args: List[JavaExpressionOrQuery], context: JavaContext): JavaExpressionOrQuery = {
    (methodName, args) match {
      case ("filter", List(arg)) => this.filter(arg, context)
      case ("limitBy", List(orderingFunction, limitingNumberFunction)) =>
        this.limitBy(orderingFunction, limitingNumberFunction, context)
//      case ("head", Nil) =>
//        this.head(context)
      case ("reduce", List(start, map, reducer)) =>
        this.reduce(start, map, reducer, context)
      case ("sum", List()) =>
        this.sum(context)
      case (_, _) =>
        throw new RuntimeException(s"trying to call $methodName")
    }
  }

  def filter(arg: JavaExpressionOrQuery, context: JavaContext): JavaExpressionOrQuery = this match {
    case UnorderedQuery(_, _, None, None) =>
      UnorderedQueryApplication(UnorderedQuery(source, whereClauses :+ WhereClause.build(arg, context), None, None))
  }

  def limitBy(ordering: JavaExpressionOrQuery, limiting: JavaExpressionOrQuery, context: JavaContext): JavaExpressionOrQuery = {
    this match {
      case UnorderedQuery(_, _, None, None) =>
        UnorderedQueryApplication(
          UnorderedQuery(source, whereClauses, Some(LimitByClause.build(ordering, limiting, context)), None))
    }
  }

  def reduce(start: JavaExpressionOrQuery, map: JavaExpressionOrQuery, reducer: JavaExpressionOrQuery, context: JavaContext) = {
    this match {
      case UnorderedQuery(_, _, _, None) =>
        UnorderedQueryApplication(
          UnorderedQuery(source, whereClauses, limiter, Some(Reduction.build(start, map, reducer, context))))
    }
  }

  def sum(context: JavaContext) = {
    val identityOnNumber = JavaLambdaExpr(List("x" -> JavaIntType), JavaVariable("x"))
    val sumOnNumber = JavaLambdaExpr(List("x" -> JavaIntType, "y" -> JavaIntType),
      JavaExpression.parse("x + y"))

    this match {
      case UnorderedQuery(_, _, _, None) =>
        UnorderedQueryApplication(
          UnorderedQuery(source, whereClauses, limiter, Some(
            Reduction.build(JavaMath(Number(0)), identityOnNumber, sumOnNumber, context))))
    }
  }

//  def head(context: JavaContext) = this match {
//    case UnorderedQuery(_, _, None, None) =>
//      UnorderedQueryApplication(
//        UnorderedQuery(source, whereClauses, Some(LimitByClause.build(JavaLambdaExpr(List"x", JavaUnit), JavaMath(Number(1)), context)), None))
//  }
}


object UnorderedQuery {
  def blank(source: String) = UnorderedQuery(source, Nil, None, None)
}
