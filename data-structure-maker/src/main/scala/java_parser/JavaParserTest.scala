package java_parser

import java.io.StringBufferInputStream
import java_transpiler.{AstBuilder, RubyOutputter}

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit

object JavaParserTest {
  def main(args: Array[String]) {
    val node = parseJavaFile("")
    val classAsts = AstBuilder.build(node)
    println(classAsts)
    println(classAsts.map(RubyOutputter.outputClass).mkString("\n\n"))

    classAsts.foreach(RubyOutputter.outputClassToFile)
  }

  def parseJavaFile(filename: String): CompilationUnit = {
    // i know this is deprecated but IDGAF
    parseJava(javaString)
  }

  def parseJava(java: String) = {
    val stringBuffer = new StringBufferInputStream(java)
    JavaParser.parse(stringBuffer)
  }

  val javaString =
    """
       |class Counter {
       |  public Counter(int start) {
       |    this.x = start;
       |    new Counter(x -> x);
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

object ParserOfApi {
  def main(args: Array[String]) {
    val cu = JavaParserTest.parseJava(
      """
        |public class PriorityQueue  {
        |    String[] fields = {"priority", "id"};
        |    // ideally I'd be able to not specify those two bools
        |    MagicMultiset stuff = new MagicMultiset(fields, true, true);
        |
        |    int getIdOfCheapest() {
        |        return stuff.orderDescendingBy(x -> x.priority).first.id;
        |    }
        |
        |    int insertItem(int priority, int id) {
        |        stuff.insert(priority, id);
        |    }
        |
        |    int popCheapest() {
        |        int cheapest = getIdOfCheapest();
        |        stuff.remove(cheapest);
        |        return cheapest;
        |    }
        |}
        |
      """.stripMargin)

    val queueMethods = AstBuilder.build(cu).head.methods.filter(_.isSuperFuckingSimple())
    queueMethods.foreach((x) => println(RubyOutputter.outputMethod(x)))
  }

}
