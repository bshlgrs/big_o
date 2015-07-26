package java_transpiler

import cas.Name
import com.github.javaparser.ast.stmt._
import scala.collection.JavaConverters._

sealed abstract class JavaStatement

case class VariableDeclarationStatement(name: String, javaType: JavaType, initialValue: Option[JavaExpression]) extends JavaStatement
case class ReturnStatement(value: JavaExpression) extends JavaStatement
case class ExpressionStatement(value: JavaExpression) extends JavaStatement
case class IfStatement(cond: JavaExpression, trueCase: List[JavaStatement], falseCase: List[JavaStatement]) extends JavaStatement
case class WhileStatement(cond: JavaExpression, action: List[JavaStatement]) extends JavaStatement

object JavaStatement {
  def buildBlock(blk: BlockStmt): List[JavaStatement] = {
    blk.getStmts.asScala.map(buildStatement).toList
  }

  def buildStatement(stmt: Statement): JavaStatement = stmt match {
    case s: ExpressionStmt => ExpressionStatement(JavaExpression.build(s.getExpression))
    case s: ReturnStmt => ReturnStatement(JavaExpression.build(s.getExpr))
    case _ =>
      println(s"$stmt : ${stmt.getClass} not implemented, fuckin do it man")
      ???
  }
}
