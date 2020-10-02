package controllers

import java.util.Date
import java.util.concurrent.TimeoutException

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import controllers.HelloActor.SayHello
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt

@Singleton
class ActorController @Inject() (cc: ControllerComponents, actorSystem: ActorSystem)(
  implicit exec: ExecutionContext
) extends AbstractController(cc)
    with Logging {

  // actorOf method is used to create a new actor
  val helloActor: ActorRef = actorSystem.actorOf(HelloActor.props, "hello-actor")

  def myHello(name: String): Action[AnyContent] = Action.async {
    implicit val timeout: Timeout = 5.seconds

    // application.conf 에 설정된 my-bounded-mailbox capacity 를 넘는 요청은 dead letter 로 처리되어 actor 로 부터 ack 가 오지 않음
    // 따라서 5초 이후에 AskTimeoutException 이 발생
    (helloActor ? SayHello(name)).recoverWith {
      case e: TimeoutException =>
        logger.error("time out", e)
        Future.successful(Ok(s"$name, server is busy"))

      case e: Exception =>
        logger.error("exception", e)
        Future.successful(Ok(s"$name, server is busy"))

    }.mapTo[String].map(message => Ok(message))
  }
}

object HelloActor {
  def props: Props = Props[HelloActor].withMailbox("my-bounded-mailbox")

  case class SayHello(name: String)
}

class HelloActor extends Actor with Logging {
  import HelloActor._

  override def receive: Receive = {
    case SayHello(name: String) => {
      logger.info("Receive Message !!!, " + new Date())
      Thread.sleep(4000L)
      sender() ! "Hello, " + name
    }
  }
}
