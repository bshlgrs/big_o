package java_transpiler

object RubyOutputter {
  def outputClass(javaClass: JavaClass): String = {
    val initializationStmts = javaClass.fields.collect({
      case x if x.initialValue.isDefined => ExpressionStatement(
        JavaAssignmentExpression(x.name, false, x.initialValue.get))
    })

    val initializationMethod = JavaMethodDeclaration(
      "initialize",
      javaClass.constructor.map(_.args).getOrElse(Nil),
      javaClass.constructor.map(_.body).getOrElse(Nil) ++ initializationStmts)

    val initializationString = outputMethod(initializationMethod)

    val fields = javaClass.fields.map({ (x) =>
      s"  # ${x.name}: ${x.javaType.toScalaTypeString()} = ${x.initialValue}"
    }).mkString("\n")

    val methodsString = javaClass.methods.map(outputMethod).mkString("\n\n")

    s"class ${javaClass.name}\n$initializationString\n$methodsString\nend"
  }

  def outputMethod(decl: JavaMethodDeclaration): String = {
    val args = if (decl.args.length == 0)
      ""
    else
      "(" ++ decl.args.map(_._1).mkString(", ") ++ ")"

    val body = decl.body.dropRight(1).map("    " + outputStatement(_, false) + "\n").mkString("")
    val lastStatementInBody = "    " + outputStatement(decl.body.last, true)

    s"  def ${decl.name}$args\n$body$lastStatementInBody\n  end"
  }

  def outputStatement(stmt: JavaStatement, isAtEnd: Boolean): String = stmt match {
    case ExpressionStatement(exp) => outputExpression(exp)
    case ReturnStatement(exp) => isAtEnd match {
      case true => outputExpression(exp)
      case false => s"return ${outputExpression(exp)}"
    }
  }

  def outputExpression(exp: JavaExpression): String = exp match {
    case JavaIntLit(n) => n.toString
    case JavaAssignmentExpression(name, isLocal, expr) =>
      isLocal match {
        case true => s"$name = ${outputExpression(expr)}"
        case false =>
          expr match {
            case thing@JavaBinaryOperation(op, lhs, rhs) if lhs == JavaFieldAccess(JavaThis, name) =>
              s"@$name ${thing.opString}= ${outputExpression(rhs)}"
            case _ =>
              s"@$name = ${outputExpression(expr)}"
          }
      }
    case JavaThis => "self"
    case JavaFieldAccess(JavaThis, field) => s"@$field"
    case JavaVariable(name) => name
    case exp@JavaBinaryOperation(op, lhs, rhs) => s"(${outputExpression(lhs)} ${exp.opString} ${outputExpression(rhs)})"
    case JavaNewObject(className, args) => s"$className.new${mbBracket(args.map(outputExpression))}"
  }

  def mbBracket(blah: List[String]) = {
    blah match {
      case Nil => ""
      case _ => "(" + blah.mkString(", ") + ")"
    }
  }
}
