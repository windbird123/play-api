package repositories
import bases.BlockingExecutionContext
import cats.syntax.all._
import models.{Author, Book}
import play.api.{Logger, MarkerContext}

import java.util.concurrent.atomic.AtomicReference
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class BookRepository @Inject() ()(implicit ec: BlockingExecutionContext) {
  private val logger = Logger(getClass)

  private val books: AtomicReference[Seq[Book]] = new AtomicReference(
    Seq(
      Book("The Sorrows of Young Werther", 1774, Author("Johann Wolfgang von Goethe")),
      Book("Iliad", -8000, Author("Homer")),
      Book("Nad Niemnem", 1888, Author("Eliza Orzeszkowa")),
      Book("The Colour of Magic", 1983, Author("Terry Pratchett")),
      Book("The Art of Computer Programming", 1968, Author("Donald Knuth")),
      Book("Pharaoh", 1897, Author("Boleslaw Prus"))
    )
  )

  def getBooks()(implicit mc: MarkerContext): Future[Either[Unit, Seq[Book]]] = Future {
    logger.info("get all books")
    books.get().asRight
  }

  def addBook(book: Book)(implicit mc: MarkerContext): Future[Either[Unit, Unit]] = Future {
    logger.info(s"add a book, ${book.title}")
    Right(books.getAndUpdate(books => books :+ book))
  }
}
