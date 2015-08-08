package java_transpiler.queries

import java_transpiler.{JavaVariable, JavaExpressionOrQuery}

import cas.MathExp

case class WhereClause(
                  nodeVariableName: String,
                  lhs: JavaExpressionOrQuery,
                  rhs: JavaExpressionOrQuery,
                  isEqualsInsteadOfGreaterThan: Boolean,
                  context: JavaContext) {

  val constantWhereClause = ConstantWhereClause.build(this)
  val separableWhereClause = SeparableWhereClause.build(this)

  def childrenExpressions(): List[JavaExpressionOrQuery] = {
    List(lhs, rhs)
  }

  lazy val freeVariables = childrenExpressions().flatMap(_.freeVariables).toSet - nodeVariableName
}

abstract class WhereClauseNiceness


class ConstantWhereClause extends WhereClauseNiceness

object ConstantWhereClause {
  def build(whereClause: WhereClause): Option[WhereClauseNiceness] = {
    if (whereClause.freeVariables.subsetOf(Set(whereClause.nodeVariableName)))
      Some(new ConstantWhereClause)
    else
      None
  }
}

case object SeparableWhereClause {
  def build(whereClause: WhereClause): Option[WhereClauseNiceness] = {
    if (whereClause.lhs.freeVariables == Set())
      Some(SeparableWhereClause(whereClause.lhs, whereClause.rhs))
    else if (whereClause.rhs.freeVariables == Set())
      Some(SeparableWhereClause(whereClause.rhs, whereClause.lhs))
    else
      None
  }
}

case class SeparableWhereClause(
             nodeFunction: JavaExpressionOrQuery,
             paramFunction: JavaExpressionOrQuery) extends ConstantWhereClause

