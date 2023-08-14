package routers

import models.MyError
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.play.jsonBody

import java.nio.charset.StandardCharsets
import javax.inject.{Inject, Singleton}

@Singleton
class EchoEndpoints @Inject() () {
  private val baseBookEndpoint = endpoint
    .tag("Echo API")

  val echoEndpoint: PublicEndpoint[String, MyError, String, Any] =
    baseBookEndpoint.get
      .in("echo")
      .in(query[String]("message"))
      .errorOut(
        oneOf[MyError](
          oneOfVariant(statusCode(StatusCode.BadRequest).and(jsonBody[MyError].description("invalid input message")))
        )
      )
      .out(plainBody[String](StandardCharsets.UTF_8))
}
