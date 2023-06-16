package models

import play.api.libs.json.{Format, Json}

case class AuthError(error: String)

object AuthError {
  implicit val format: Format[AuthError] = Json.format[AuthError]
}

case class AuthenticatedContext(userId: String)
