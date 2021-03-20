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
  val tps   = config.get[Int]("service.tps")

  // Filter 에서도 제한할 수 있지만 action.AroundAction 처럼 action composition 을 통해 제한할 수도 있다.
  override def apply(nextFilter: EssentialAction) = EssentialAction { implicit request =>
    // marker logging 을 위해, request 로 부터 암시적으로 MarkerContext 를 생성해 줌
    import libs.application._

    val inCount = count.incrementAndGet()
    logger.info(s"Before Filter: $inCount,  tps=$tps")

    if (inCount < tps) {
      nextFilter(request).recover { case NonFatal(e) => Results.Ok(s"failed to handle request in RequestLimitFilter") }.map { result =>
        val out = result.withHeaders("X-ExampleFilter" -> "foo")

        val outCount = count.decrementAndGet()
        logger.info(s"After Filter: $outCount")

        out
      }
    } else {
      val afterCount = count.decrementAndGet()
      Accumulator.done(Future.successful(Results.Ok(s"afterCount=$afterCount")))
    }
  }
}
