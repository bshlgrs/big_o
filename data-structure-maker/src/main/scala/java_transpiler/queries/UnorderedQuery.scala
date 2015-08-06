package java_transpiler.queries

import java_transpiler.JavaExpressionOrQuery

import cas.Name

class UnorderedQuery(val sourceColumns: List[Name],
            val parameters: List[Name],
            val whereClauses: WhereClauses,
            val limiter: Either[LimitByClause, OrderByClause],
            val reduction: Reduction) extends JavaExpressionOrQuery {
  
  def applyJavaMethod(methodName: String, args: List[JavaExpressionOrQuery]): UnorderedQuery = {
    ???
  }
}
