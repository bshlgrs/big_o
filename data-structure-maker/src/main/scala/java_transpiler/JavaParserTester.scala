package java_transpiler

import japa.parser.JavaParser
import java.io.StringBufferInputStream

object JavaParserTester {
  def main(args: Array[String]) {
    val stringBuffer = new StringBufferInputStream("class HelloWorld {}")
    val x = JavaParser.parse(stringBuffer)
    println(x)
  }
}
