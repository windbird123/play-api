package bases

import org.slf4j.MarkerFactory
import play.api.MarkerContext

import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

object RequestMarkerContext {
  private val serverId: String = UUID.randomUUID().toString.substring(0, 8)
  private val incrementNumber: AtomicLong = new AtomicLong(0L)
  private def newRequestId: String = serverId + "_" + incrementNumber.incrementAndGet()

  def newMarkerContext: MarkerContext = MarkerFactory.getMarker(newRequestId)
}
