package services

import javax.inject.{Inject, Singleton}
import libs.MarkerLogging
import org.slf4j.MarkerFactory
import play.api.mvc.RequestHeader
import play.api.{Configuration, Logging, MarkerContext}

trait MyService {
//  implicit val request: RequestHeader
  def print()(implicit request: RequestHeader): Unit
}

object MyObj extends MyService {
  override def print()(implicit request: RequestHeader): Unit = println("MY obj inst")
}

@Singleton
class MyServiceImpl @Inject() (config: Configuration) extends MyService with MarkerLogging {

  // 주의: marker logging 을 위해 implicit request: RequestHeader 이 있어야 한다..
  override def print()(implicit request: RequestHeader): Unit = {
    logger.info("Hi my proj: " + config.get[String]("service.name"))
  }
}
