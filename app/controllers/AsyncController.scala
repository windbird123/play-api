package controllers

import akka.actor.ActorSystem
import javax.inject._
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
class AsyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem, myService: MyService)(implicit exec: ExecutionContext)
    extends AbstractController(cc) {
  import libs.PlayZio._
  def message = Action.z { request =>
    myService.print()

    import zio._
    Task.effectTotal("Hi222").map(msg => Ok(msg))
  }
}
