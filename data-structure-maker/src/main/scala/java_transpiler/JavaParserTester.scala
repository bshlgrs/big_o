package java_transpiler

import japa.parser.JavaParser
import java.io.StringBufferInputStream

object JavaParserTester {
  def main(args: Array[String]) {
    // i know this is deprecated but IDGAF
    val stringBuffer = new StringBufferInputStream(javaString)
    val x = JavaParser.parse(stringBuffer)
    println(x)
  }

  val javaString =
    """
       class Counter {
         int x = 0;
         void increment() {
           this.x += 1;
         }
         int get() {
           return this.x;
         }
       }
    """

}
