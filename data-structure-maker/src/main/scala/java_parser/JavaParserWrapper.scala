package java_parser

import java.io.StringBufferInputStream
import java_transpiler._

import ast_renderers.RubyOutputter
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit

import scala.collection.mutable

object JavaParserWrapper {
  def main(args: Array[String]) {
    val node = parseJavaFile("")
    val classAsts = AstBuilder.build(node)
    println(classAsts)
    println(classAsts.map(RubyOutputter.outputClass).mkString("\n\n"))

    classAsts.foreach(RubyOutputter.outputClassToFile)
  }

  def parseJavaFile(filename: String): CompilationUnit = {
    parseJava(javaString)
  }

  def parseJava(java: String) = {
    // i know this is deprecated but IDGAF
    val stringBuffer = new StringBufferInputStream(java)
    JavaParser.parse(stringBuffer)
  }

  def parseJavaClassToAst(java: String): JavaClass = {
    AstBuilder.build(parseJava(java)).head
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
    val priorityQueue = JavaParserWrapper.parseJavaClassToAst(
      """
        |public class PriorityQueue  {
        |    class Item {
        |        int priority;
        |        int id;
        |    }
        |
        |    MagicMultiset<Item> stuff = new MagicMultiset<Item>(true, true);
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
      """.stripMargin)

    println(priorityQueue.methodsCalledOnObject("stuff"))

    val buffer = new mutable.ListBuffer[String]()

    val methodsCalledOnObjectVisitor = new AstModifier(List(_), {
      case x@ JavaMethodCall(JavaVariable("stuff"), name, _) => { buffer.prepend(name) ; x}
      case x => x
    })

//    priorityQueue.methods.foreach(_.)
  }

}
