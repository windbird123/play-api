package services

import akka.stream.Materializer
import cats.syntax.all._
import models.{AuthError, Book}
import play.api.cache.AsyncCacheApi
import play.api.{Logger, MarkerContext}
import repositories.BookRepository

import javax.inject._
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BookService @Inject() (bookRepository: BookRepository, cache: AsyncCacheApi)(implicit
    mat: Materializer,
    ec: ExecutionContext
) {
  private val logger = Logger(getClass)

  def listBooks()(implicit mc: MarkerContext): Future[Either[Unit, Seq[Book]]] = {
    logger.info("SERVICE: listBooks()")

    cache.getOrElseUpdate("all_list", expiration = 1.minute) {
      bookRepository.getBooks()
    }
  }

  def addBook(book: Book)(implicit mc: MarkerContext): Future[Either[AuthError, Unit]] = {
    logger.info("SERVICE: addBook()")
    val added: Future[Either[Unit, Unit]] = bookRepository.addBook(book)
    added.map(_.leftMap(_ => AuthError("auth error")))
  }
}
