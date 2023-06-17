package bases

import scala.concurrent.Future
import scala.util.Try

object MonadConverter {
  implicit class EitherToFuture[A](either: Either[Throwable, A]) {
    def toFuture: Future[A] = Future.fromTry(either.toTry)
  }

  implicit class TryToFuture[A](t: Try[A]) {
    def toFuture: Future[A] = Future.fromTry(t)
  }
}
