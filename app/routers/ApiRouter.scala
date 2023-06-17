package routers

import akka.stream.Materializer
import bases.RequestMarkerContext
import models.{AuthenticatedContext, Book}
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.{Logger, MarkerContext}
import services.BookService
import sttp.apispec.openapi.circe.yaml._
import sttp.apispec.openapi.{Info, OpenAPI}
import sttp.tapir.docs.openapi._
import sttp.tapir.server.play.{PlayServerInterpreter, PlayServerOptions}
import sttp.tapir.swagger.SwaggerUI

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApiRouter @Inject() (
    bookEndpoints: BookEndpoints,
    bookController: BookService
)(implicit
    val mat: Materializer,
    ec: ExecutionContext
) extends SimpleRouter {
  private val logger = Logger(getClass)
  private val playServerOptions: PlayServerOptions = PlayServerOptions.default(mat, ec)
  private val interpreter = PlayServerInterpreter(playServerOptions)

  private val openApiRoute: Routes = {
    val openApiDocs: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(
      List(bookEndpoints.booksListingEndpoint, bookEndpoints.addBookEndpoint.endpoint),
      Info("Tapir Play API", "1.0.0")
    )
    interpreter.toRoutes(SwaggerUI[Future](openApiDocs.toYaml))
  }

  private val booksListingRoute: Routes = interpreter.toRoutes(
    bookEndpoints.booksListingEndpoint
      .serverLogic { _ =>
        implicit val mc: MarkerContext = RequestMarkerContext.newMarkerContext

        bookController.listBooks()
      }
  )

  private val addBookRoute: Routes = interpreter.toRoutes(
    bookEndpoints.addBookEndpoint
      .serverLogic { (authenticatedContext: AuthenticatedContext) => (book: Book) =>
        {
          implicit val mc: MarkerContext = RequestMarkerContext.newMarkerContext

          logger.info(s"ApiRouter: Authenticated with userId=${authenticatedContext.userId}")
          bookController.addBook(book)
        }
      }
  )

  // Routes are partial functions
  override def routes: Routes = openApiRoute
    .orElse(booksListingRoute)
    .orElse(addBookRoute)
}
