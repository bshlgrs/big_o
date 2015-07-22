package java_transpiler

import japa.parser.JavaParser
import java.io.StringBufferInputStream
import scala.collection.JavaConverters._

import japa.parser.ast.CompilationUnit

object JavaToAst {
  def main(args: Array[String]) {
    val node = parseJavaFile("")

    println(AstBuilder.build(node))
  }

  def parseJavaFile(filename: String): CompilationUnit = {
    // i know this is deprecated but IDGAF
    println(javaString)
    println("... turns into")
    val stringBuffer = new StringBufferInputStream(javaString)
    JavaParser.parse(stringBuffer)
  }

  val javaString =
    """
       class Counter {
         int x = 0;
         void increase(int y) {
           this.x += y;
         }
         int get() {
           return this.x;
         }
       }
    """
}
