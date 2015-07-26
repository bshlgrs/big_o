package queries

import cas.MathExp

class OrderByClause {

}

case class GoodOrderByClause(nodeExpr: MathExp[DataSource]) extends OrderByClause
case class BadOrderByClause(expr: MathExp[DataSource]) extends OrderByClause
