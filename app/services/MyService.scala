package services

import javax.inject.{Inject, Singleton}
import libs.MarkerLogging
import play.api.{Configuration, MarkerContext}

trait MyService {
  def print()(implicit mc: MarkerContext): Unit
}

// impl
@Singleton
class MyServiceImpl @Inject() (config: Configuration) extends MyService with MarkerLogging {
  // 주의: marker logging 을 위해 implicit request: RequestHeader 가 scope 안에 이 있어야
  // MarkerLogging.requestHeaderToMarkerContext 에 의해 MarkerContext 로 변환될 수 있다.
  override def print()(implicit mc: MarkerContext): Unit =
    logger.info("Hi my proj: " + config.get[String]("service.name"))
}
