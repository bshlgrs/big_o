package queries

import cas.MathExp

class Reduction {


}

case object SelectStar extends Reduction
case class FoldReduction(start: MathExp[DataSource], mapper: MathExp[DataSource], reducer: MathExp[DataSource]) extends Reduction
