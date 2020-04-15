package filters

import javax.inject._
import libs.MarkerLogging
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
 * This is a simple filter that adds a header to all requests. It's
 * added to the application's list of filters by the
 * [[Filters]] class.
 *
 * @param ec This class is needed to execute code asynchronously.
 * It is used below by the `map` method.
 */
@Singleton
class ExampleFilter @Inject() (implicit ec: ExecutionContext) extends EssentialFilter with MarkerLogging {
  override def apply(next: EssentialAction) = EssentialAction { implicit request =>
    logger.info("Before Filter")
    next(request).map { result =>
      val out = result.withHeaders("X-ExampleFilter" -> "foo")
      logger.info("After 222")
      out
    }
  }
}
