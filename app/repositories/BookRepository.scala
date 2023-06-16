package repositories
import cats.syntax.all._
import models.{Author, Book}

import java.util.concurrent.atomic.AtomicReference
import javax.inject.Singleton

@Singleton
class BookRepository {
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

  def getBooks(): Either[Unit, Seq[Book]] = books.get().asRight

  def addBook(book: Book): Either[Unit, Unit] = Right(books.getAndUpdate(books => books :+ book))
}
