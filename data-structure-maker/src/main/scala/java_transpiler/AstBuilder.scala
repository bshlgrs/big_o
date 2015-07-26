package java_transpiler

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.{ConstructorDeclaration, MethodDeclaration, FieldDeclaration, TypeDeclaration}

import scala.collection.JavaConverters._

object AstBuilder {
  def build(cu: CompilationUnit): List[JavaClass] = {
    cu.getTypes.asScala.map(buildClass).toList
  }

  def buildClass(typeDeclaration: TypeDeclaration): JavaClass = {
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

    JavaClass(typeDeclaration.getName, constructorAst, fieldDeclarationAsts, methodDeclarationAsts)
  }
}
