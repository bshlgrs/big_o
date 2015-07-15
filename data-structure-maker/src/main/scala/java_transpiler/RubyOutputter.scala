package java_transpiler

import japa.parser.ast.CompilationUnit
import scala.collection.JavaConverters._

object RubyOutputter {
  def output(cu: CompilationUnit): String = {
    cu.getTypes.asScala.map { (node) =>
      s"class ${node.getName}\nend"
    }.mkString("\n\n")
  }
}
