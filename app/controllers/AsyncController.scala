package controllers

import javax.inject._
import akka.actor.ActorSystem
import akka.util.ByteString
import play.api.mvc._
import services.MyService

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

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
class AsyncController @Inject()(
    cc: ControllerComponents,
    actorSystem: ActorSystem,
    myService: MyService
)(implicit exec: ExecutionContext)
    extends AbstractController(cc) {

  /**
   * Creates an Action that returns a plain text message after a delay
   * of 1 second.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/message`.
   */
  def message2 = Action.async {
    getFutureMessage(1.second).map { msg =>
      Ok(msg)
    }
  }

  import MyZio._
  def message = Action.z { request =>
//    request.body.asRaw.map { buffer =>
//      buffer.asBytes(999999).toArray[Byte]
//    }
    myService.print()

    import zio._
    Task.effectTotal("Hi222").map(msg => Ok(msg))
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Hi!")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

}

object MyZio {
  implicit class ActionBuilderOps[+R[_], B](
      actionBuilder: ActionBuilder[R, B]
  ) {
    def z(zioAction: R[B] => zio.Task[Result]): Action[B] =
      actionBuilder.async { request =>
        val task = zioAction(request)
        zio.Runtime.default.unsafeRunToFuture(task)
      }
  }
}
