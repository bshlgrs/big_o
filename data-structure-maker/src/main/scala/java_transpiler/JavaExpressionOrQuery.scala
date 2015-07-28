package java_transpiler

// this class is implemented in JavaExpression and Query.
abstract class JavaExpressionOrQuery {
  def descendantExpressions: List[JavaExpressionOrQuery] = ???

  def querify(): JavaExpressionOrQuery = {
    ???
  }
}
