package queries

import cas.Name

sealed abstract class DataSource

case class QueryThing(query: Query) extends DataSource
case class ParamVariable(name: Name) extends DataSource
case class Field(name: Name) extends DataSource
