package controllers

import com.github.windbird123.helloworld.grpc.{ GreeterServiceClient, HelloReply, HelloRequest }
import com.typesafe.config.Config
import javax.inject._
import play.api.Configuration
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (
  cc: ControllerComponents,
  greeterServiceClient: GreeterServiceClient,
  config: Configuration
)(
  implicit assetsFinder: AssetsFinder,
  ec: ExecutionContext
) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def home = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def index = Action.async {
    val request = HelloRequest("Caplin")
    // create a gRPC request
    val reply: Future[HelloReply] = greeterServiceClient.sayHello(request)
    // forward the gRPC response back as a plain String on an HTTP response
    reply.map(_.message).map(m => Ok(m))
  }

}
