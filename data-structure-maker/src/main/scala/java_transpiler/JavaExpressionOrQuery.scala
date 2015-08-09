package java_transpiler

import java_transpiler.queries.{JavaContext, UnorderedQuery}

// this class is implemented in JavaExpression and Query.
abstract class JavaExpressionOrQuery {
  def descendantExpressions(): List[JavaExpressionOrQuery] = {
    this +: childrenExpressions().flatMap(_.descendantExpressions())
  }

  def childrenExpressions(): List[JavaExpressionOrQuery]

  // this is overwritten by lambda, obviously
  def freeVariables: Set[String] = {
    childrenExpressions().flatMap(_.freeVariables).toSet
  }

  def querify(javaContext: JavaContext): JavaExpressionOrQuery

  def modify(astModifier: AstModifier): JavaExpressionOrQuery = astModifier.applyToExpr(this)
}

case class UnorderedQueryApplication(unorderedQuery: UnorderedQuery) extends JavaExpressionOrQuery {
  override def childrenExpressions() = unorderedQuery.childrenExpressions()

  def querify(javaContext: JavaContext) = this
  
  
}
