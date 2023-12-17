package routers

import bases.RequestMarkerContext
import models.{AuthenticatedContext, Book}
import org.apache.pekko.stream.Materializer
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.{Logger, MarkerContext}
import services.{BookService, EchoService}
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
    bookService: BookService,
    echoEndpoints: EchoEndpoints,
    echoService: EchoService
)(implicit
    val mat: Materializer,
    ec: ExecutionContext
) extends SimpleRouter {
  private val logger = Logger(getClass)
  private val playServerOptions: PlayServerOptions = PlayServerOptions.default(mat, ec)
  private val interpreter = PlayServerInterpreter(playServerOptions)

  private val openApiRoute: Routes = {
    val openApiDocs: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(
      List(
        bookEndpoints.booksListingEndpoint,
        bookEndpoints.getBookEndpoint,
        bookEndpoints.addBookEndpoint.endpoint,
        echoEndpoints.echoEndpoint
      ),
      Info("Tapir Play API", "1.0.0")
    )
    interpreter.toRoutes(SwaggerUI[Future](openApiDocs.toYaml))
  }

  private val booksListingRoute: Routes = interpreter.toRoutes(
    bookEndpoints.booksListingEndpoint
      .serverLogic { case (start, limit) =>
        implicit val mc: MarkerContext = RequestMarkerContext.newMarkerContext

        println(s"start=[$start], limit=[$limit]")
        bookService.listBooks()
      }
  )

  private val getBookRoute: Routes = interpreter.toRoutes(
    bookEndpoints.getBookEndpoint
      .serverLogicRecoverErrors { title: String =>
        implicit val mc: MarkerContext = RequestMarkerContext.newMarkerContext

        logger.info(s"ApiRouter: getBookRoute, title=$title")
        bookService.getBook(title)
      }
  )

  private val addBookRoute: Routes = interpreter.toRoutes(
    bookEndpoints.addBookEndpoint
      .serverLogic { (authenticatedContext: AuthenticatedContext) => (book: Book) =>
        {
          implicit val mc: MarkerContext = RequestMarkerContext.newMarkerContext

          logger.info(s"ApiRouter: Authenticated with userId=${authenticatedContext.userId}")
          bookService.addBook(book)
        }
      }
  )

  private val echoRoute: Routes = interpreter.toRoutes(
    echoEndpoints.echoEndpoint.serverLogicRecoverErrors { message: String =>
      implicit val mc: MarkerContext = RequestMarkerContext.newMarkerContext

      logger.info(s"ApiRouter: echoRoute, message=$message")
      echoService.echo(message)
    }
  )

  // Routes are partial functions
  override def routes: Routes = openApiRoute
    .orElse(booksListingRoute)
    .orElse(getBookRoute)
    .orElse(addBookRoute)
    .orElse(echoRoute)
}
