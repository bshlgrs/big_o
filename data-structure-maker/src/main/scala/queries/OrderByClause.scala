package queries

import cas.MathExp

class OrderByClause {

}

case class GoodOrderByClause(nodeExpr: MathExp[FML]) extends OrderByClause
case class BadOrderByClause(expr: MathExp[FML]) extends OrderByClause
