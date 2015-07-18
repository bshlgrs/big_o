package java_transpiler

import japa.parser.ast.body._
import japa.parser.ast._
import scala.collection.JavaConverters._

object RubyOutputter {
  def output(cu: CompilationUnit): String = {
    cu.getTypes.asScala.map(printClass).mkString("\n\n")
  }

  def printClass(n: TypeDeclaration) = {
    val fields = n.getMembers.asScala

    val methods = fields.filter(_.isInstanceOf[MethodDeclaration]).map(_.asInstanceOf[MethodDeclaration])

    val methodsString = methods.map(outputMethod).mkString("\n\n")

    s"class ${n.getName}\n$methodsString\nend"
  }

  def outputMethod(methodDeclaration: MethodDeclaration): String = {
    val argsString = Option(methodDeclaration.getParameters) match {
      case None => ""
      case Some(x) => {
        "(" ++ x.asScala.map({(x) =>
          x.getId.getName
        }).mkString(", ") ++ ")"
      }
    }

    s"  def ${methodDeclaration.getName}$argsString\n\n  end"
  }
}
