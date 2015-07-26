package java_transpiler

import com.github.javaparser.ast.body._


case class JavaFieldDeclaration(name: String, javaType: JavaType, initialValue: Option[JavaExpression] = None)

object JavaFieldDeclaration {
  def build(fieldDeclaration: FieldDeclaration): JavaFieldDeclaration = {
    val list = fieldDeclaration.getChildrenNodes
    val javaType = JavaType.build(fieldDeclaration.getType)

    list.size match {
      case 2 => (list.get(0), list.get(1)) match {
        case (_type, dec: VariableDeclarator) =>
          JavaFieldDeclaration(dec.getId.getName, javaType, Some(JavaExpression.build(dec.getInit)))
        case _ => ???
      }
      case _ => ???
    }

  }
}
