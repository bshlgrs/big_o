package java_transpiler.queries

import java_transpiler.{JavaLambdaExpr, JavaMath, JavaExpressionOrQuery}

import cas.MathExp

case class LimitByClause(nodeVariableName: String,
                         orderingFunction: JavaExpressionOrQuery,
                         limitingFunction: JavaExpressionOrQuery) {
  def childrenExpressions() = List(orderingFunction, limitingFunction)

  lazy val freeVariables: Set[String] = childrenExpressions().flatMap(_.freeVariables).toSet - nodeVariableName

  val constantSizeLimitBy = ConstantSizeLimitBy.build(this)
}

object LimitByClause {
  def build(ordering: JavaExpressionOrQuery, limiting: JavaExpressionOrQuery, context: JavaContext) = {
    ordering match {
      case JavaLambdaExpr(List(name), body) =>
        LimitByClause(name._1, body, limiting)
      case _ => throw new RuntimeException(s"this is not valid for a limit by: $ordering")
    }
  }
}

abstract class LimitByClauseNiceness

case class ConstantSizeLimitBy(size: Int) extends LimitByClauseNiceness

object ConstantSizeLimitBy {
  // This should work even if the number isn't actually constant, but it's constant over the time period we care about
  def build(limitByClause: LimitByClause): Option[ConstantSizeLimitBy] = limitByClause.limitingFunction match {
    case JavaMath(mathExp: MathExp[JavaExpressionOrQuery]) => mathExp match {
      case n: cas.Number[_] => Some(ConstantSizeLimitBy(n.value))
    }
    case _ => None
  }
}

object PurelyNodeBasedOrderingFunction extends LimitByClauseNiceness {
  def build(limitByClause: LimitByClause): Option[LimitByClauseNiceness] = {
    if (limitByClause.freeVariables.subsetOf(Set(limitByClause.nodeVariableName)))
      Some(this)
    else
      None
  }
}
