package services

import javax.inject.{ Inject, Singleton }
import play.api.{ Configuration, Logging, MarkerContext }
import zio.{ Has, IO, RIO, UIO, ZIO }

trait MyService {
  def print()(implicit mc: MarkerContext): UIO[Unit]
}

// impl
@Singleton
class MyServiceImpl @Inject() (config: Configuration) extends MyService with Logging {
  override def print()(implicit mc: MarkerContext): UIO[Unit] = IO.effectTotal {
    logger.info("Hi my serviceImpl: " + config.get[String]("service.name"))
  }
}
