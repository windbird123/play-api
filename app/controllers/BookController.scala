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
    bookRepository.getBooks()
  }

  def addBook(book: Book): Future[Either[AuthError, Unit]] = {
    val added: Future[Either[Unit, Unit]] = bookRepository.addBook(book)
    added.map(_.leftMap(_ => AuthError("auth error")))
  }
}
