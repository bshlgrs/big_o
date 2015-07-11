package queries

import cas.Name

class Query(val parameters: List[Name],
            val whereClauses: WhereClauses,
            val limiter: Either[LimitByClause, OrderByClause],
            val reduction: Reduction) {
  
}
