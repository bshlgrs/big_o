package java_transpiler

import com.github.javaparser.ast.body._
import scala.collection.JavaConverters._

case class JavaClass(name: String,
                     constructor: Option[JavaConstructorDeclaration],
                     fields: List[JavaFieldDeclaration],
                     methods: List[JavaMethodDeclaration],
                     innerClasses: List[JavaClass]) {
  def allSuperFuckingSimpleMethodNames(): List[String] = {
    methods.filter(_.isSuperFuckingSimple()).map(_.name)
  }

  def getMethod(name: String): Option[JavaMethodDeclaration] = {
    methods.find(_.name == name)
  }

  def getField(name: String): Option[JavaFieldDeclaration] = {
    fields.find(_.name == name)
  }
}

object JavaClass {
  def build(typeDeclaration: TypeDeclaration): JavaClass = {
    val fieldDeclarations = typeDeclaration
      .getMembers
      .asScala
      .filter(_.isInstanceOf[FieldDeclaration])
      .map(_.asInstanceOf[FieldDeclaration])

    val fieldDeclarationAsts = fieldDeclarations.map(JavaFieldDeclaration.build).toList

    val methodDeclarations = typeDeclaration
      .getMembers
      .asScala
      .filter(_.isInstanceOf[MethodDeclaration])
      .map(_.asInstanceOf[MethodDeclaration])

    val methodDeclarationAsts = methodDeclarations.map(JavaMethodDeclaration.build).toList

    val constructorAst = typeDeclaration
      .getMembers
      .asScala
      .filter(_.isInstanceOf[ConstructorDeclaration])
      .map(_.asInstanceOf[ConstructorDeclaration])
      .headOption
      .map(JavaMethodDeclaration.maybeBuildConstructor)

    val innerClasses = typeDeclaration
      .getMembers
      .asScala
      .filter(_.isInstanceOf[ClassOrInterfaceDeclaration])
      .map(_.asInstanceOf[ClassOrInterfaceDeclaration])
      .map(build)
      .toList

    JavaClass(typeDeclaration.getName, constructorAst, fieldDeclarationAsts, methodDeclarationAsts, innerClasses)
  }

}
