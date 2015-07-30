package java_transpiler

import com.github.javaparser.ast.expr._
import com.github.javaparser.ast.stmt.ExpressionStmt
import scala.collection.JavaConverters._
import scala.util.Try

case class JavaBinaryOperation(op: BinaryExpr.Operator, lhs: JavaExpressionOrQuery, rhs: JavaExpressionOrQuery) extends JavaExpressionOrQuery {
  lazy val opString = op match {
    case BinaryExpr.Operator.plus => "+"
    case BinaryExpr.Operator.times => "*"
    case BinaryExpr.Operator.equals => "=="
    case BinaryExpr.Operator.greaterEquals => ">="
  }
}

sealed abstract class JavaExpression extends JavaExpressionOrQuery

case object JavaNull extends JavaExpression
case class JavaIntLit(item: Int) extends JavaExpression
case class JavaBoolLit(boolean: Boolean) extends JavaExpression
case class JavaMethodCall(callee: JavaExpressionOrQuery, methodName: String, args: List[JavaExpressionOrQuery]) extends JavaExpression
case class JavaFieldAccess(thing: JavaExpressionOrQuery, field: String) extends JavaExpression
case class JavaNewObject(className: String, typeArgs: List[JavaType], args: List[JavaExpressionOrQuery]) extends JavaExpression
case object JavaThis extends JavaExpression
case class JavaVariable(name: String) extends JavaExpression
case class JavaIfExpression(cond: JavaExpressionOrQuery, ifTrue: JavaExpressionOrQuery, ifFalse: JavaExpressionOrQuery) extends JavaExpression
// maybe the next line is a massive mistake :/
case class JavaLambdaExpr(args: List[(String, JavaType)], out: JavaExpressionOrQuery) extends JavaExpression
case object JavaUnit extends JavaExpression
case class JavaAssignmentExpression(name: String, local: Boolean, expression: JavaExpressionOrQuery) extends JavaExpression
case class JavaArrayInitializerExpr(items: List[JavaExpressionOrQuery]) extends JavaExpression
case class JavaStringLiteral(string: String) extends JavaExpression

// OH SHIT OH SHIT OH SHIT I BET THAT I WILL REGRET THIS LATER

object JavaExpression {
  def build(exp: Expression): JavaExpression = exp match {
    case null => ???
    case exp: IntegerLiteralExpr =>
      JavaIntLit(exp.getValue.toInt)
    case exp: AssignExpr =>
      val (lhs, isLocal) = exp.getTarget match {
        case f: FieldAccessExpr => (f.getField, false)
        case _ =>
          ???
      }

      val mbOp = exp.getOperator match {
        case AssignExpr.Operator.assign => None
        case AssignExpr.Operator.plus => Some(BinaryExpr.Operator.plus)
        case AssignExpr.Operator.minus => Some(BinaryExpr.Operator.minus)
      }

      val outExp = mbOp match {
        case None => build(exp.getValue)
        case Some(op) => JavaBinaryOperation(op, JavaFieldAccess(JavaThis, lhs), build(exp.getValue))
      }
      JavaAssignmentExpression(lhs, isLocal, outExp)
    case exp: BinaryExpr =>
      JavaBinaryOperation(exp.getOperator, build(exp.getLeft), build(exp.getRight)).asInstanceOf[JavaExpression]
    //    case exp: MethodCallExpr =>
    //      ???
    case exp: NameExpr => JavaVariable(exp.getName)
    case exp: FieldAccessExpr => JavaFieldAccess(build(exp.getScope), exp.getField)
    case exp: ThisExpr => JavaThis
    case exp: ObjectCreationExpr =>
      val javaArgs = Option(exp.getArgs).map(_.asScala.toList)
      val args = javaArgs.getOrElse(List())
      val name = exp.getType.getName
      val typeArgs = Option(exp.getTypeArgs).map(_.asScala.toList).getOrElse(Nil).map(JavaType.build)
      JavaNewObject(name, typeArgs, args.map(build))
    case exp: LambdaExpr => exp.getBody match {
      case stmt: ExpressionStmt =>
        val params = exp.getParameters.asScala.map(_.getId.getName -> JavaIntType).toList
        JavaLambdaExpr(params, build(stmt.getExpression))
      case _ =>
        throw new RuntimeException("I can't deal with non-expression contents of lambdas yet. Oh well, neither can Python.")
    }
    case exp: ArrayInitializerExpr =>
      JavaArrayInitializerExpr(Option(exp.getValues).map(_.asScala.map(build)).getOrElse(Nil).toList)
    case exp: StringLiteralExpr =>
      JavaStringLiteral(exp.getValue)
    case exp: BooleanLiteralExpr =>
      JavaBoolLit(exp.getValue)
    case exp: MethodCallExpr =>
      val args = Try(exp.getArgs.asScala.toList).getOrElse(Nil).map(build)
      val scope = Option(exp.getScope).map(build).getOrElse(JavaThis)
      JavaMethodCall(scope, exp.getName, args)
    case exp: VariableDeclarationExpr =>
      throw new RuntimeException("this case should be handled in the JavaStatement#build method :/")
    case exp: NullLiteralExpr =>
      JavaNull
    case _ =>
      println(s"$exp : ${exp.getClass} not implemented, do it man")
      ???
  }


}
