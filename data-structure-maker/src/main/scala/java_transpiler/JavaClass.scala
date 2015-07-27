package java_transpiler

case class JavaClass(name: String,
                     constructor: Option[JavaConstructorDeclaration],
                     fields: List[JavaFieldDeclaration],
                     methods: List[JavaMethodDeclaration]) {
  def allSuperFuckingSimpleMethodNames(): List[String] = {
    methods.filter(_.isSuperFuckingSimple()).map(_.name)
  }
}
