package libs

import java.util.UUID
import org.slf4j.MarkerFactory
import play.api.MarkerContext
import play.api.mvc._
import zio.{Has, Layer, RIO, ZLayer}

package object playzio {
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
  // for zio action
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  implicit class ActionBuilderOps[+R[_], B](actionBuilder: ActionBuilder[R, B]) {
    def z(zioAction: R[B] => RIO[Has[MarkerContext], Result]): Action[B] = actionBuilder.async { request =>
      val mcLayer: Layer[Nothing, Has[MarkerContext]] =
        ZLayer.succeed(request.asInstanceOf[RequestHeader].toMarkerContext)

      val task = zioAction(request).provideLayer(mcLayer)
      zio.Runtime.default.unsafeRunToFuture(task)
    }

    def z[A](bp: BodyParser[A])(zioAction: R[A] => RIO[Has[MarkerContext], Result]): Action[A] =
      actionBuilder(bp).async { request =>
        val mcLayer: Layer[Nothing, Has[MarkerContext]] =
          ZLayer.succeed(request.asInstanceOf[RequestHeader].toMarkerContext)

        val task = zioAction(request).provideLayer(mcLayer)
        zio.Runtime.default.unsafeRunToFuture(task)
      }
  }
}
