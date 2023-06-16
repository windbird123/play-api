package controllers

import akka.stream.Materializer
import models.{AuthError, AuthenticatedContext}
import repositories.BookRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SecureController @Inject() (bookRepository: BookRepository)(implicit mat: Materializer, ec: ExecutionContext) {
  def authenticateToken(bearer: String): Future[Either[AuthError, AuthenticatedContext]] = {
    Future {
      if (bearer == "SecretKey") {
        Right(AuthenticatedContext("JohnDoe"))
      } else {
        Left(AuthError("Wrong bearer"))
      }
    }
  }
}
