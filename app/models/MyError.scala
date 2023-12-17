package models

import play.api.libs.json.{Format, Json}

case class MyError(msg: String) extends Exception

object MyError {
  implicit val format: Format[MyError] = Json.format[MyError]
}
