package queries

import cas.Expression

class OrderByClause {

}

case class GoodOrderByClause(nodeExpr: Expression) extends OrderByClause
case class BadOrderByClause(expr: Expression) extends OrderByClause
