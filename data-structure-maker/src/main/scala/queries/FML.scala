package queries

import cas.Name

sealed abstract class FML

case class QueryThing(query: Query) extends FML
case class ParamVariable(name: Name) extends FML
case class Field(name: Name) extends FML
