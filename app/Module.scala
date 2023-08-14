import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import services.{EchoService, EchoServiceImpl}

class Module(environment: Environment, config: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[EchoService]).to(classOf[EchoServiceImpl])
//    bind(classOf[EchoService]).toInstance(...)
  }
}
