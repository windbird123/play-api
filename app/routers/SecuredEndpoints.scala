package routers

import models.{AuthError, AuthenticatedContext}
import services.SecureService
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.play.jsonBody
import sttp.tapir.server.PartialServerEndpoint

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class SecuredEndpoints @Inject() (secureController: SecureService) {
  private val securedWithBearerEndpoint: Endpoint[String, Unit, AuthError, Unit, Any] = endpoint
    .securityIn(auth.bearer[String]())
    .errorOut(statusCode(StatusCode.Unauthorized))
    .errorOut(jsonBody[AuthError])

  val securedWithBearer: PartialServerEndpoint[String, AuthenticatedContext, Unit, AuthError, Unit, Any, Future] =
    securedWithBearerEndpoint
      .serverSecurityLogic(secureController.authenticateToken)
}
