package cas_caller

import scalaj.http._

object CasWrapper {
  def variables(exp: String): List[String] = {
    val response: HttpResponse[String] = Http("http://127.0.0.1:7856/variables").postData(exp).asString
    response.body.split(",").toList
  }
}
