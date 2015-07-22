package java_transpiler

import cas._
import japa.parser.ast.body.{ConstructorDeclaration, MethodDeclaration}
import scala.collection.JavaConverters._

case class JavaMethodDeclaration(name: String, args: List[(String, JavaType)], body: List[JavaStatement])

case class JavaConstructorDeclaration(args: List[(String, JavaType)], body: List[JavaStatement])

object JavaMethodDeclaration {
  def build(methodDeclaration: MethodDeclaration) = {
    val name = methodDeclaration.getName

    val args = Option(methodDeclaration.getParameters).map(_.asScala.toList.map({ (x) =>
      (x.getId.getName, JavaType.build(x.getType))})).getOrElse(List())

    val body = JavaStatement.buildBlock(methodDeclaration.getBody)

    JavaMethodDeclaration(name, args, body)
  }

  def maybeBuildConstructor(decl: ConstructorDeclaration): JavaConstructorDeclaration = {
    val args = Option(decl.getParameters).map(_.asScala.toList.map({ (x) =>
      (x.getId.getName, JavaType.build(x.getType))})).getOrElse(List())

    val body = JavaStatement.buildBlock(decl.getBlock)

    JavaConstructorDeclaration(args, body)
  }
}
