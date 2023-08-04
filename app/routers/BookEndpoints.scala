package routers

import models.{AuthError, AuthenticatedContext, Author, Book}
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.play.jsonBody
import sttp.tapir.server.PartialServerEndpoint

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import models.MyError

@Singleton
class BookEndpoints @Inject() (securedEndpoints: SecuredEndpoints) {
  private val baseBookEndpoint = endpoint
    .tag("Books API")
    .in("books")

  private val baseSecuredBookEndpoint
      : PartialServerEndpoint[String, AuthenticatedContext, Unit, AuthError, Unit, Any, Future] =
    securedEndpoints.securedWithBearer
      .tag("Books API")
      .in("books")

  val booksListingEndpoint: PublicEndpoint[(Int, Option[Int]), MyError, Seq[Book], Any] =
    baseBookEndpoint.get
      .summary("List all books")
      .in("list" / "all")
      .in(query[Int]("start") and query[Option[Int]]("limit"))
      .errorOut(
        oneOf[MyError](
          oneOfVariant[MyError](statusCode(StatusCode.BadRequest).and(jsonBody[MyError]))
        )
      )
      .out(jsonBody[Seq[Book]])

  val getBookEndpoint: PublicEndpoint[String, MyError, Book, Any] =
    baseBookEndpoint.get
      .summary("Get specific book")
      .in("get")
      .in(query[String]("title"))
      .errorOut(
        oneOf[MyError](
          oneOfVariant(statusCode(StatusCode.NotFound).and(jsonBody[MyError].description("book not found")))
        )
      )
      .out(jsonBody[Book])

  val addBookEndpoint: PartialServerEndpoint[String, AuthenticatedContext, Book, AuthError, Unit, Any, Future] =
    baseSecuredBookEndpoint.post
      .summary("Add a book")
      .in("add")
      .in(
        jsonBody[Book]
          .description("The book to add")
          .example(Book("Pride and Prejudice", 1813, Author("Jane Austen")))
      )
      .out(statusCode(StatusCode.Created))
}
