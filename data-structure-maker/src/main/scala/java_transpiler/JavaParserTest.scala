package java_transpiler

import japa.parser.JavaParser
import java.io.StringBufferInputStream
import scala.collection.JavaConverters._

import japa.parser.ast.CompilationUnit

object JavaParserTest {
  def main(args: Array[String]) {
    val node = parseJavaFile("")
    val classAsts = AstBuilder.build(node)
    println(classAsts)
    println(classAsts.map(RubyOutputter.outputClass).mkString("\n\n"))
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
       |class Counter {
       |  public Counter(int start) {
       |    this.x = start;
       |    new Counter();
       |  }
       | 
       |  void increase(int y) {
       |    this.x += y;
       |  }
       |  int get() {
       |    return this.x;
       |  }
       |}
    """.stripMargin('|')
}
