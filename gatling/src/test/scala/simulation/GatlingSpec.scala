package simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

/**
 * step1) intellij 에서 run configuration 을 만들어 rest api 서버 실행 (Shift + F10)
 * step2) sbt shell 에서 아래 실행
 *   a) project gatling
 *   b) gatling:test
 * step3) target/gatling/gatlingspec-xxx 하위에 있는 index.html 을 열어서 확인
 */
class GatlingSpec extends Simulation {
  val httpConf: HttpProtocolBuilder = http
    .baseUrl("http://localhost:9000")
    .acceptEncodingHeader("gzip,deflate")

  val indexReq = repeat(10) {
    exec(
      http("message req").get("/message").check(status.is(200))
    )
  }

  val rootReq = repeat(10) {
    exec(
      http("root req").get("/").check(status.is(200))
    )
  }

  val myScn  = scenario("message scn").exec(indexReq).pause(1.second).exec(rootReq)
  val myScn2 = scenario("message scn 2").exec(indexReq, rootReq)

  setUp(
    // 3 명의 user 가 2초에 걸쳐 들어와 myScn 실행
    myScn.inject(rampUsers(3).during(2.seconds)).protocols(httpConf),
    // 3 명의 user 가 동시에 들어와 myScn 실행
    myScn2.inject(atOnceUsers(2)).protocols(httpConf)
  )
}
