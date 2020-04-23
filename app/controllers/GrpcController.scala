package controllers

import com.github.windbird123.helloworld.grpc.{GreeterServiceClient, HelloReply, HelloRequest}
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.InjectedController

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GrpcController @Inject() (greeterServiceClient: GreeterServiceClient, config: Configuration)(
  implicit ec: ExecutionContext
) extends InjectedController {
  def index = Action.async {
    val request = HelloRequest("Caplin")
    // create a gRPC request
    val reply: Future[HelloReply] = greeterServiceClient.sayHello(request)
    // forward the gRPC response back as a plain String on an HTTP response
    reply.map(_.message).map(m => Ok(m))
  }
}
