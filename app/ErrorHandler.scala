import javax.inject.{Inject, Provider, Singleton}
import common.application._
import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._
import play.api.routing.Router

import scala.concurrent._

/**
 * Provides a stripped down error handler that does not use HTML in error pages, and
 * prints out debugging output.
 *
 * https://www.playframework.com/documentation/latest/ScalaErrorHandling
 */

@Singleton
class ErrorHandler @Inject() (
  env: Environment,
  config: Configuration,
  sourceMapper: OptionalSourceMapper,
  router: Provider[Router]
) extends DefaultHttpErrorHandler(env, config, sourceMapper, router)
    with Logging {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    implicit val mc: MarkerContext = request.toMarkerContext

    logger.info(s"onClientError: statusCode = $statusCode, uri = ${request.uri}, message = $message")

    Future.successful {
      val result = statusCode match {
        case BAD_REQUEST =>
          Results.BadRequest(message)
        case FORBIDDEN =>
          Results.Forbidden(message)
        case NOT_FOUND =>
          Results.NotFound(message)
        case clientError if statusCode >= 400 && statusCode < 500 =>
          Results.Status(statusCode)
        case nonClientError =>
          val msg =
            s"onClientError invoked with non client error status code $statusCode: $message"
          throw new IllegalArgumentException(msg)
      }
      result
    }
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    implicit val mc: MarkerContext = request.toMarkerContext
    super.onServerError(request, exception)
  }

  override protected def onDevServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    implicit val mc: MarkerContext = request.toMarkerContext
    Future.successful(InternalServerError(Json.obj("exception" -> exception.toString)))
  }

  override protected def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    implicit val mc: MarkerContext = request.toMarkerContext
    Future.successful(InternalServerError)
  }
}
