package queries

import java_transpiler.JavaExpression

import cas.Name

class Query(val sourceColumns: List[Name],
            val parameters: List[Name],
            val whereClauses: WhereClauses,
            val limiter: Either[LimitByClause, OrderByClause],
            val reduction: Reduction) {

  def applyJavaMethod(methodName: String, args: List[JavaExpression]): Query = {
    ???
  }
}
