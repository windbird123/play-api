package controllers

import akka.actor.ActorSystem
import javax.inject._
import org.slf4j.{ MDC, MarkerFactory }
import play.api.{ Logging, MarkerContext }
import play.api.mvc._
import services.MyService

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param cc standard controller components
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.  When rendering content, you should use Play's
 * default execution context, which is dependency injected.  If you are
 * using blocking operations, such as database or network access, then you should
 * use a different custom execution context that has a thread pool configured for
 * a blocking API.
 */
@Singleton
class AsyncController @Inject() (cc: ControllerComponents, actorSystem: ActorSystem, myService: MyService)(
  implicit exec: ExecutionContext
) extends AbstractController(cc)
    with Logging {

  import libs.PlayZio._

  // implicit request 로 해야 PlayZio.requestHeaderToMarkerContext 가 적용되어 log 에 UUID 가 기록된다.
  def message = Action.z { implicit request =>
    logger.info("TEST logging with marker")
    myService.print()

    import zio._
    Task.effectTotal {
      logger.info("log in zio task")
      "Hi222"
    }.map(msg => Ok(msg))
  }
}
