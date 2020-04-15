package filters

import javax.inject._
import play.api.Logging
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
class ExampleFilter @Inject() (implicit ec: ExecutionContext) extends EssentialFilter with Logging {

  override def apply(next: EssentialAction) = EssentialAction { request =>
    logger.info("Before 111111111")
    next(request).map { result =>
      println("lenBody: " + result.body.contentLength)
      val out = result.withHeaders("X-ExampleFilter" -> "foo")
      println("After 222")
      out
    }
  }
}
