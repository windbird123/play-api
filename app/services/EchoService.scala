package services

import org.apache.pekko.actor.ActorSystem
import play.api.{Logger, MarkerContext}

import javax.inject._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

trait EchoService {
  def echo(message: String)(implicit mc: MarkerContext): Future[String]
}

@Singleton
class EchoServiceImpl @Inject() (actorSystem: ActorSystem)(implicit ec: ExecutionContext) extends EchoService {
  private val logger = Logger(getClass)

  override def echo(message: String)(implicit mc: MarkerContext): Future[String] = {
    logger.info("SERVICE: echo()")

    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(1.second) {
      promise.success(s"After 1 second, $message")
    }
    promise.future
  }
}
