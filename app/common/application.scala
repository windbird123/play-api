package common

import org.slf4j.MarkerFactory
import play.api.MarkerContext
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import zio.Task

import java.util.UUID

object application {
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  // marker context logging: implicit RequestHeader 가 있으면 MarkerContext 가 제공됨
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  val serverStartId: String = UUID.randomUUID().toString.replace("-", "").substring(0, 8)

  implicit class MarkerLoggingOpt(request: RequestHeader) {
    def toMarkerContext: MarkerContext = requestHeaderToMarkerContext(request)
  }

  implicit def requestHeaderToMarkerContext(implicit request: RequestHeader): MarkerContext =
    MarkerFactory.getMarker(serverStartId + "-" + request.id)

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  // json converter
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  implicit class ToJson[T: Writes](x: T) {
    def toJson: JsValue = Json.toJson[T](x)
  }

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  // for zio action
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  implicit class ActionBuilderOps[+R[_], B](actionBuilder: ActionBuilder[R, B]) {
    def task(f: R[B] => Task[Result]): Action[B] = actionBuilder.async { request =>
      val task = f(request)
      zio.Runtime.default.unsafeRunToFuture(task)
    }

    def task[A](bp: BodyParser[A])(f: R[A] => Task[Result]): Action[A] = actionBuilder(bp).async { request =>
      val task = f(request)
      zio.Runtime.default.unsafeRunToFuture(task)
    }
  }
}
