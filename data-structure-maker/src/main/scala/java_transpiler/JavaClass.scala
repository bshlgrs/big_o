package java_transpiler

case class JavaClass(name: String,
                     constructor: Option[JavaConstructorDeclaration],
                     fields: List[JavaFieldDeclaration],
                     methods: List[JavaMethodDeclaration]) {
  def allSuperFuckingSimpleMethodNames(): List[String] = {
    methods.filter(_.isSuperFuckingSimple()).map(_.name)
  }

  def getMethod(name: String): Option[JavaMethodDeclaration] = {
    methods.find(_.name == name)
  }

  def getField(name: String): Option[JavaFieldDeclaration] = {
    fields.find(_.name == name)
  }
}


