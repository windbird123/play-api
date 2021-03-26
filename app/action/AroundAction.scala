package action

import java.util.concurrent.atomic.AtomicLong

import play.api.Logging
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

object AroundAction {
  val counter = new AtomicLong(0L)
}

case class AroundAction[A](action: Action[A]) extends Action[A] with Logging {
  def apply(request: Request[A]): Future[Result] = {
    // for marker logging
    import common.application._
    implicit val r: Request[A] = request

    logger.info(s"Counting: ${AroundAction.counter.incrementAndGet()}")
    action(request)
  }

  override def parser: BodyParser[A]              = action.parser
  override def executionContext: ExecutionContext = action.executionContext
}
