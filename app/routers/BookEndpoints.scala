package routers

import models.{AuthError, AuthenticatedContext, Author, Book}
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.play.jsonBody
import sttp.tapir.server.PartialServerEndpoint

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

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

  val booksListingEndpoint: PublicEndpoint[Unit, Unit, Seq[Book], Any] =
    baseBookEndpoint.get
      .summary("List all books")
      .in("list" / "all")
      .out(jsonBody[Seq[Book]])

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
