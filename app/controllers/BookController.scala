package controllers

import akka.stream.Materializer
import cats.syntax.all._
import models.{AuthError, Book}
import repositories.BookRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BookController @Inject() (bookRepository: BookRepository)(implicit mat: Materializer, ec: ExecutionContext) {
  def listBooks(): Future[Either[Unit, Seq[Book]]] = {
    Future.successful(bookRepository.getBooks())
  }

  def addBook(book: Book): Future[Either[AuthError, Unit]] = {
    Future.successful(bookRepository.addBook(book).leftMap(_ => AuthError("auth error")))
  }
}
