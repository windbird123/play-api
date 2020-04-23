package routers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.github.windbird123.helloworld.grpc.{AbstractGreeterServiceRouter, HelloReply, HelloRequest}
import javax.inject.Inject

import scala.concurrent.Future

class HelloWorldRouter @Inject() (mat: Materializer, system: ActorSystem) extends AbstractGreeterServiceRouter(mat, system) {

  // We need to inject a Materializer since it is required by the abstract
  // router. It can also be used to access the ExecutionContext if you need
  // to transform Futures. For example:
  //
  // private implicit val matExecutionContext = mat.executionContext
  //
  // But at this example, this is not necessary.


  override def sayHello(in: HelloRequest): Future[HelloReply] =
    Future.successful(HelloReply(s"Hello, ${in.name}!"))
}
