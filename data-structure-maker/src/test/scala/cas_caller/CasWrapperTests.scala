package cas_caller

import org.scalatest.{Matchers, FlatSpec}

class CasWrapperTests extends FlatSpec with Matchers {

  "CasWrapper" should "tell you the variables in an expression" in {
    CasWrapper.variables("a + x + b") should be(Set("a","b","x"))
  }

//  it should "throw NoSuchElementException if an empty stack is popped" in {
//    val emptyStack = new Stack[Int]
//    a [NoSuchElementException] should be thrownBy {
//      emptyStack.pop()
//    }
//  }
}
