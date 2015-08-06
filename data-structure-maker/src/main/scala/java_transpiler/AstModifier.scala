package java_transpiler

case class AstModifier(stmtMapper: JavaStatement => List[JavaStatement], exprMapper: JavaExpressionOrQuery => JavaExpressionOrQuery) {
  def applyToStmt(stmt: JavaStatement): List[JavaStatement] = stmtMapper(mapOverStmt(stmt))
  def applyToExpr(expr: JavaExpressionOrQuery): JavaExpressionOrQuery = exprMapper(mapOverExpr(expr))

  private def apply(thing: Any): Any = thing match {
    case expr: JavaExpressionOrQuery => applyToExpr(expr)
    case stmt: JavaStatement => applyToStmt(stmt)
  }

  def mapOverStmt(stmt: JavaStatement): JavaStatement = stmt match {
    case VariableDeclarationStatement(name, javaType, initialValue) =>
      VariableDeclarationStatement(name, javaType, initialValue.map(applyToExpr))
    case ReturnStatement(value) => ReturnStatement(applyToExpr(value))
    case ExpressionStatement(exp) => ExpressionStatement(applyToExpr(exp))
    case IfStatement(cond, trueCase, falseCase) =>
      IfStatement(applyToExpr(cond), trueCase.flatMap(applyToStmt), falseCase.flatMap(applyToStmt))
    case WhileStatement(cond, action) =>
      WhileStatement(applyToExpr(cond), action.flatMap(applyToStmt))
  }

  def mapOverExpr(expr: JavaExpressionOrQuery): JavaExpressionOrQuery = expr match {
    case _ => ???
  }
}
