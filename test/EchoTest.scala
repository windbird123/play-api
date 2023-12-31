import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.{Application, Configuration, MarkerContext}
import repositories.BookRepository
import services.EchoService

import scala.concurrent.Future

class FakeEchoService extends EchoService {
  override def echo(message: String)(implicit mc: MarkerContext): Future[String] = {
    Future.successful(s"FAKE echo: $message")
  }
}

class EchoTest extends PlaySpec with GuiceOneAppPerSuite with BeforeAndAfter {
  // app 에서 inject 된 BookRepository 참조해 보기
  lazy val repo: BookRepository = app.injector.instanceOf[BookRepository]

  lazy val overrideModules = Seq(
    bind[EchoService].to[FakeEchoService]
  )

  // NOTE: class 단위로 테스트를 실행해야 before 가 수행됨
  // NOTE: "XXX" should { "YYY" in { .. } } 마다 before 가 수행됨
  // NOTE: 특정 test 를 스킵하려면 in 대신에 ignore 를 사용
  //       "XXX" should { "YYY" ignore { .. } }
  before {}

  after {}

  override def fakeApplication(): Application = {
    val underlying: Config = ConfigFactory.load("application-test.conf")

    new GuiceApplicationBuilder()
      .disable[play.filters.csrf.CSRFFilter]
      .loadConfig(Configuration(underlying)) // application-test.conf 으로 conf 교체해 app 실행하기
      .overrides(overrideModules) // module override
      .build()
  }

  "echo api 호출" should {
    "EchoServiceImpl 대신에 FakeEchoService 로 동작해 응답 되어야 한다." in {
      val request = FakeRequest(GET, "/echo?message=ABC")
      val result: Future[Result] = route(app, request).get
      val response = contentAsString(result)

      response mustBe "FAKE echo: ABC"
    }
  }
}
