package java_transpiler

import japa.parser.ast.`type`.{ClassOrInterfaceType, PrimitiveType, Type}
import scala.collection.JavaConverters._

sealed abstract class JavaType {
  def toScalaTypeString(): String
}

case object JavaIntType extends JavaType {
  lazy val toScalaTypeString = "Int"

}
case class JavaClassType(name: String, itemTypes: List[JavaType]) extends JavaType {
  lazy val toScalaTypeString = s"$name[${itemTypes.map(_.toScalaTypeString()).mkString(", ")}]"
}

object JavaType {
  def build(thing: Type): JavaType = {
    thing match {
      case x: PrimitiveType =>
        x.getType match {
          case PrimitiveType.Primitive.Int => JavaIntType
          case _ => ???
        }
      case x: ClassOrInterfaceType =>
        JavaClassType(x.getName, x.getTypeArgs.asScala.map(JavaType.build).toList)
    }
  }

}
