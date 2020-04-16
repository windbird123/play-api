package filters

import java.util.concurrent.atomic.AtomicInteger

import javax.inject._
import play.api.libs.streams.Accumulator
import play.api.mvc._
import play.api.{Configuration, Logging}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

/**
 * This is a simple filter that adds a header to all requests. It's
 * added to the application's list of filters by the
 * [[Filters]] class.
 *
 * @param ec This class is needed to execute code asynchronously.
 * It is used below by the `map` method.
 */
@Singleton
class RequestLimitFilter @Inject() (config: Configuration)(implicit ec: ExecutionContext)
    extends EssentialFilter
    with Logging {
  val count = new AtomicInteger(0)

  override def apply(nextFilter: EssentialAction) = EssentialAction { implicit request =>
    // marker logging 을 위해
    import libs.playzio._

    val tps     = config.get[Int]("service.tps")
    val inCount = count.incrementAndGet()
    logger.info(s"Before Filter: $inCount,  tps=$tps")

    if (inCount < tps) {
      nextFilter(request).recover { case NonFatal(e) => Results.Ok(s"count=$count") }.map { result =>
        val out = result.withHeaders("X-ExampleFilter" -> "foo")

        val outCount = count.decrementAndGet()
        logger.info(s"After Filter: $outCount")

        out
      }
    } else {
      Accumulator.done(Future.successful(Results.Ok(s"count=$count")))
    }
  }
}
