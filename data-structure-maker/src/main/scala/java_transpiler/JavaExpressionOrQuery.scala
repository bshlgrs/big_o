package java_transpiler

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

  def querify(): JavaExpressionOrQuery = {
    ???
  }

  def modify(astModifier: AstModifier): JavaExpressionOrQuery = astModifier.applyToExpr(this)
}
