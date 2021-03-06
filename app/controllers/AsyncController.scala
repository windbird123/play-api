package controllers

import akka.actor.ActorSystem
import javax.inject._
import play.api.Logging
import play.api.libs.json.{Json, OFormat}
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

  import common.application._
  // implicit request 로 해야 libs.playzio.requestHeaderToMarkerContext 가 적용되어 log 에 UUID 가 기록된다.
  def message = Action.task { implicit request =>
    logger.info("TEST logging with marker")
    val svc = myService.print()

    import zio._
    svc *> Task.effectTotal {
      logger.info("log in zio task")
      MyResponse("kjm", 21)
    }.map(msg => Ok(Json.toJson(msg)))
  }

  // action composition !!!
  def message2 = action.AroundAction(message)
}

case class MyResponse(name: String, age: Int)
object MyResponse {
  implicit val myResponseFormat: OFormat[MyResponse] = Json.format[MyResponse]
}
