package libs

import java.util.UUID

import org.slf4j.MarkerFactory
import play.api.mvc.RequestHeader
import play.api.{Logging, MarkerContext}

trait MarkerLogging extends Logging {
  // converted Marker to MarkerContext by implicit
  implicit def requestHeaderToMarkerContext(implicit request: RequestHeader): MarkerContext =
    MarkerFactory.getMarker(MarkerLogging.serverStartId + "-" + request.id)
}

object MarkerLogging extends MarkerLogging {
  val serverStartId: String = UUID.randomUUID().toString.replace("-", "").substring(0, 8)
}
