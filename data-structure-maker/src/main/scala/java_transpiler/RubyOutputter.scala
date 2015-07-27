package java_transpiler

import java.io.{PrintWriter, File}

object RubyOutputter {
  def outputClassToFile(javaClass: JavaClass) = {
    val writer = new PrintWriter(new File(s"target/ruby/${javaClass.name}.rb" ))

    writer.write(outputClass(javaClass))
    writer.close()
  }

  def outputClass(javaClass: JavaClass): String = {
    val initializationStmts = javaClass.fields.collect({
      case x if x.initialValue.isDefined => ExpressionStatement(
        JavaAssignmentExpression(x.name, false, x.initialValue.get))
    })

    val initializationString = if (javaClass.constructor.isDefined || javaClass.fields.exists(_.initialValue.isDefined)) {
      val initializationMethod = JavaMethodDeclaration(
        "initialize",
        javaClass.constructor.map(_.args).getOrElse(Nil),
        javaClass.constructor.map(_.body).getOrElse(Nil) ++ initializationStmts)
      outputMethod(initializationMethod)
    }
    else
      ""

    val fields = javaClass.fields.map({ (x) =>
      s"  # ${x.name}: ${x.javaType.toScalaTypeString()} = ${x.initialValue}"
    }).mkString("\n")

    val methodsString = javaClass.methods.map(outputMethod).mkString("\n\n")

    s"class ${javaClass.name}\n$initializationString\n$methodsString\nend"
  }

  def outputMethod(decl: JavaMethodDeclaration): String = {
    val args = mbBracket(decl.args.map(_._1))

    val body = decl.body.dropRight(1).map("    " + outputStatement(_, false) + "\n").mkString("")
    val lastStatementInBody = "    " + outputStatement(decl.body.last, true)

    s"  def ${decl.name}$args\n$body$lastStatementInBody\n  end"
  }

  def outputStatement(stmt: JavaStatement, isAtEnd: Boolean): String = {
    val code = stmt match {
      case ExpressionStatement(exp) => outputExpression(exp)
      case ReturnStatement(exp) => isAtEnd match {
        case true => outputExpression(exp)
        case false => s"return ${outputExpression(exp)}"
      }
      case VariableDeclarationStatement(name, _, initialValue) => initialValue match {
        case Some(value) => "name = " + outputExpression(value)
        case None => ""
      }
      case _ =>
        throw new RuntimeException(s"ruby needs to have a output for ${stmt.getClass}")
    }

    (isAtEnd, stmt) match {
      case (_, _: ReturnStatement) => code
      case (false, _) => code
      case (true, _) => code + "\nnil"
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
    case JavaLambdaExpr(args, body) => args match {
      case Nil => throw new RuntimeException("Hey, I don't allow side effects in lambdas right now, so this is bad")
      case _ => s"{ |${args.map(_._1).mkString(", ")}| ${outputExpression(body)} }"
    }
    case JavaThis => "self"
    case JavaVariable(name) => name
    case exp@JavaBinaryOperation(op, lhs, rhs) => s"(${outputExpression(lhs)} ${exp.opString} ${outputExpression(rhs)})"
    case JavaNewObject(className, args) => s"$className.new${mbBracket(args.map(outputExpression))}"
    case JavaArrayInitializerExpr(items) => "[" + items.map(outputExpression).mkString(", ") + "]"
    case JavaStringLiteral(x) => "\"" + x + "\""
    case JavaBoolLit(x) => x.toString
    case JavaFieldAccess(JavaThis, field) => s"@$field"
    case JavaFieldAccess(scope, field) => outputExpression(scope) + "." + field
    case JavaMethodCall(scope, field, args) => outputExpression(scope) + "." + field + mbBracket(args.map(outputExpression))
//    case JavaVariableDeclarationExpression(name, _, local, expr) =>
//      outputExpression(JavaAssignmentExpression(name, local, expr))
    case _ =>
      println(s"you need to implement ${exp.getClass}")
      ???
  }

  def mbBracket(blah: List[String]) = {
    blah match {
      case Nil => ""
      case _ => "(" + blah.mkString(", ") + ")"
    }
  }
}
