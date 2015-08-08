package java_transpiler.queries

import java_transpiler.JavaExpressionOrQuery

import cas.Name

class UnorderedQuery(val sourceColumns: List[Name],
            val parameters: List[Name],
            val whereClauses: WhereClauses,
            val limiter: Either[LimitByClause, OrderByClause],
            val reduction: Reduction) {

  def childrenExpressions(): List[JavaExpressionOrQuery] = {
    whereClauses.clauses.flatMap(_.childrenExpressions())
  }

  def applyJavaMethod(methodName: String, args: List[JavaExpressionOrQuery]): UnorderedQuery = {
    ???
  }
}

class UnorderedQueryApplication(unorderedQuery: UnorderedQuery, args: List[JavaExpressionOrQuery]) extends JavaExpressionOrQuery {
  override def childrenExpressions() = unorderedQuery.childrenExpressions()
}
