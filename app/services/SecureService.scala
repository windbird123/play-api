package services

import akka.stream.Materializer
import models.{AuthError, AuthenticatedContext}
import play.api.{Logger, MarkerContext}
import repositories.BookRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SecureService @Inject() (bookRepository: BookRepository)(implicit mat: Materializer, ec: ExecutionContext) {
  private val logger = Logger(getClass)

  def authenticateToken(bearer: String)(implicit mc: MarkerContext): Future[Either[AuthError, AuthenticatedContext]] = {
    logger.info("SERVICE: authenticateToken()")
    Future {
      if (bearer == "SecretKey") {
        Right(AuthenticatedContext("JohnDoe"))
      } else {
        Left(AuthError("Wrong bearer"))
      }
    }
  }
}
