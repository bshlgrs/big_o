
import java.lang.Math
import scala.util.Random

object Timing {

  val sizes = (0 until 6).flatMap((x) => List(1, 2, 5).map(_ * Math.pow(10, x))).map(Math.round(_))

  val numberOfTrials = 1

  def runTest() = {
    val results = for {
      trial <- (0 until numberOfTrials)
      size <- sizes
    } yield {
      var myArray: Array[Int] = Seq.fill(size.toInt)(Random.nextInt(1000)).toArray

      System.gc()

      val t0 = System.nanoTime()

      myArray.contains(1001)

      val t1 = System.nanoTime()

      (size, t1 - t0)
    }

    print(results)
  }

  def main(args: Array[String]) = {
    runTest()
  }
}