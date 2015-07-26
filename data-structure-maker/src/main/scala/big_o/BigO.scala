package big_o

case class BigO(powerOfN: Int, sqrtOfN: Boolean, powerOfLogN: Int) {
  assert(powerOfN >= 0)
  assert(powerOfLogN >= 0)

  override def toString = {
    if (this == Constant)
      "O(1)"
    else {
      val firstStringList = powerOfN match {
        case 0 => Nil
        case 1 => List("n")
        case n => List(s"n**$powerOfN")
      }

      val secondStringList = if (sqrtOfN)
        List(s"sqrt(n)")
      else
        Nil

      val thirdStringList = powerOfLogN match {
        case 0 => Nil
        case 1 => List("log n")
        case n => List(s"(log n)**$powerOfN")
      }

      "O(" + List(firstStringList, secondStringList, thirdStringList).flatten.mkString(" ") + ")"
    }
  }

  def *(other: BigO): BigO = {
    val extraThing = if (this.sqrtOfN && other.sqrtOfN) 1 else 0
    BigO(
      this.powerOfN + other.powerOfN + extraThing,
      this.sqrtOfN != other.sqrtOfN,
      this.powerOfLogN + other.powerOfLogN)
  }
}

object Linear extends BigO(1, false, 0)
object Quadratic extends BigO(2, false, 0)
object Logarithmic extends BigO(0, false, 1)
object Linearithmic extends BigO(1, false, 1)
object Constant extends BigO(0, false, 0)

object Tester {
  def main(args: Array[String]) {
    println(Linear, Quadratic, Constant, Linearithmic)
  }
}
