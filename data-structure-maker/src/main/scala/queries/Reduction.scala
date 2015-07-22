package queries

import cas.MathExp

class Reduction {


}

case object SelectStar extends Reduction
case class FoldReduction(start: MathExp[FML], mapper: MathExp[FML], reducer: MathExp[FML]) extends Reduction
