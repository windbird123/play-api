package models

import play.api.libs.json.{Json, Writes}

case class MyError(msg: String) extends Exception

object MyError {
  implicit val format = Json.format[MyError]
}
