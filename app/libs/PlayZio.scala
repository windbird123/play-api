package libs

import java.util.UUID

import org.slf4j.MarkerFactory
import play.api.MarkerContext
import play.api.mvc._

object PlayZio {
  implicit class ActionBuilderOps[+R[_], B](actionBuilder: ActionBuilder[R, B]) {
    def z(zioAction: R[B] => zio.Task[Result]): Action[B] = actionBuilder.async { request =>
      val task = zioAction(request)
      zio.Runtime.default.unsafeRunToFuture(task)
    }

    def z[A](bp: BodyParser[A])(zioAction: R[A] => zio.Task[Result]): Action[A] = actionBuilder(bp).async { request =>
      val task = zioAction(request)
      zio.Runtime.default.unsafeRunToFuture(task)
    }
  }

  val serverStartId: String = UUID.randomUUID().toString.replace("-", "").substring(0, 8)
  implicit def requestHeaderToMarkerContext(implicit request: RequestHeader): MarkerContext =
    MarkerFactory.getMarker(serverStartId + "-" + request.id)
}
