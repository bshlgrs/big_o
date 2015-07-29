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
  def directDescendantExpressions: List[JavaExpressionOrQuery] = this match {
    case s: VariableDeclarationStatement => s.initialValue.toList
    case s: ReturnStatement => List(s.value)
    case s: ExpressionStatement => List(s.value)
    case s: IfStatement => List(s.cond)
    case s: WhileStatement => List(s.cond)
  }

  def descendantExpressions: List[JavaExpressionOrQuery] = descendantStatements.flatMap(_.directDescendantExpressions)
}

case class VariableDeclarationStatement(name: String, javaType: JavaType, initialValue: Option[JavaExpressionOrQuery]) extends JavaStatement
case class ReturnStatement(value: JavaExpressionOrQuery) extends JavaStatement
case class ExpressionStatement(value: JavaExpressionOrQuery) extends JavaStatement
case class IfStatement(cond: JavaExpressionOrQuery, trueCase: List[JavaStatement], falseCase: List[JavaStatement]) extends JavaStatement
case class WhileStatement(cond: JavaExpressionOrQuery, action: List[JavaStatement]) extends JavaStatement

object JavaStatement {
  def buildBlock(blk: BlockStmt): List[JavaStatement] = {
    Option(blk.getStmts).map(_.asScala.toList).getOrElse(Nil).map(buildStatement)
  }

  def buildPotentiallyBlock(stmt: Statement): List[JavaStatement] = stmt match {
    case blk: BlockStmt => buildBlock(blk)
    case _ => List(buildStatement(stmt))
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
    case s: IfStmt =>
      val cond = JavaExpression.build(s.getCondition)
      val thenCase = JavaStatement.buildPotentiallyBlock(s.getThenStmt)
      val elseCase = JavaStatement.buildPotentiallyBlock(s.getElseStmt)
      IfStatement(cond, thenCase, elseCase)
    case s: ReturnStmt => ReturnStatement(JavaExpression.build(s.getExpr))
    case _ =>
      println(s"$stmt : ${stmt.getClass} not implemented, you should do it")
      ???
  }
}
