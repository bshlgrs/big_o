package java_transpiler

import cas.Name
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr._
import com.github.javaparser.ast.stmt._
import scala.collection.JavaConverters._
import scala.util.Try

sealed abstract class JavaStatement {
  def descendantStatements: List[JavaStatement] = this match {
    case _: VariableDeclarationStatement => List(this)
    case _: ReturnStatement => List(this)
    case _: ExpressionStatement => List(this)
    case IfStatement(cond, trueCase, falseCase) => trueCase ++ falseCase
    case WhileStatement(cond, action) => action
  }
  def descendantExpressions: List[JavaExpressionOrQuery] = this match {
    case _ => ???
  }
}

case class VariableDeclarationStatement(name: String, javaType: JavaType, initialValue: Option[JavaExpressionOrQuery]) extends JavaStatement
case class ReturnStatement(value: JavaExpressionOrQuery) extends JavaStatement
case class ExpressionStatement(value: JavaExpressionOrQuery) extends JavaStatement
case class IfStatement(cond: JavaExpressionOrQuery, trueCase: List[JavaStatement], falseCase: List[JavaStatement]) extends JavaStatement
case class WhileStatement(cond: JavaExpressionOrQuery, action: List[JavaStatement]) extends JavaStatement

object JavaStatement {
  def buildBlock(blk: BlockStmt): List[JavaStatement] = {
    blk.getStmts.asScala.map(buildStatement).toList
  }

  def buildStatement(stmt: Statement): JavaStatement = stmt match {
    case s: ExpressionStmt => {
      s.getExpression match {
        case e: VariableDeclarationExpr =>
          val name = e.getVars.get(0).getId.getName
          val javaType = JavaType.build(e.getType)

          val body = Try(e.getChildrenNodes.get(1).asInstanceOf[VariableDeclarator].getInit).toOption
          VariableDeclarationStatement(name, javaType, body.map(JavaExpression.build))
        case e: Expression => ExpressionStatement(JavaExpression.build(e))
      }
    }
    case s: ReturnStmt => ReturnStatement(JavaExpression.build(s.getExpr))
    case _ =>
      println(s"$stmt : ${stmt.getClass} not implemented, fuckin do it man")
      ???
  }
}
