package data_structure_handlers

import java_transpiler.{JavaStatement, JavaClass, JavaMethodDeclaration}
import big_o.BigO

class GenericDataStructureForMultiset(insertion: Option[MutatingMethodImplementation],
                           removal: Option[MutatingMethodImplementation],
                           time: JavaMethodDeclaration,
                           initialization: List[JavaStatement],
                           implementation: JavaMethodDeclaration) {

}

object GenericDataStructureForMultiset {
  def build(javaClass: JavaClass): GenericDataStructureForMultiset = {
    val insertion = javaClass.getField("timeForInsert").map { (time) =>
      val time = BigO.fromJavaExpression(time)
      val beforeInsert = javaClass.getMethod("beforeInsert").map(_.body).getOrElse(Nil)
      val afterInsert = javaClass.getMethod("afterInsert").map(_.body).getOrElse(Nil)

      MutatingMethodImplementation(time, beforeInsert, afterInsert)
    }

    val removal = javaClass.getField("timeForRemove").map { (time) =>
      val time = BigO.fromJavaExpression(time)
      val beforeInsert = javaClass.getMethod("beforeRemove").map(_.body).getOrElse(Nil)
      val afterInsert = javaClass.getMethod("afterRemove").map(_.body).getOrElse(Nil)

      MutatingMethodImplementation(time, beforeInsert, afterInsert)
    }

    val time = javaClass.getMethod("timeForQuery").getOrElse(throw new RuntimeException("needs timeForQuery"))

    val initialization = javaClass.getMethod("afterInitialize").map(_.body).getOrElse(Nil)

    val implementation = javaClass.getMethod("query").getOrElse(throw new RuntimeException("needs query"))

    new GenericDataStructureForMultiset(insertion, removal, time, initialization, implementation)
  }
}