package queries

import cas.Expression

class Reduction {


}

case object SelectStar extends Reduction
case class FoldReduction(start: Expression, mapper: Expression, reducer: Expression) extends Reduction
