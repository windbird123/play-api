package services

import cats.syntax.all._
import models.{AuthError, Book, MyError}
import org.apache.pekko.stream.Materializer
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

  // Future[Either[E,A]] 보다는 아래의 getBook() 처럼 Future[A] 가 심플한 듯?
  def listBooks()(implicit mc: MarkerContext): Future[Either[MyError, Seq[Book]]] = {
    logger.info("SERVICE: listBooks()")

    cache.getOrElseUpdate("all_list", expiration = 1.minute) {
      bookRepository.getBooks()
    }
//    Future.successful(MyError("KK").asLeft[Seq[Book]])
  }

  def getBook(title: String)(implicit mc: MarkerContext): Future[Book] = {
    logger.info("SERVICE: getBook()")
    bookRepository.getBook(title)
  }

  def addBook(book: Book)(implicit mc: MarkerContext): Future[Either[AuthError, Unit]] = {
    logger.info("SERVICE: addBook()")
    val added: Future[Either[Unit, Unit]] = bookRepository.addBook(book)
    added.map(_.leftMap(_ => AuthError("auth error")))
  }
}
