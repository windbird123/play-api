package services

import javax.inject.{ Inject, Singleton }
import play.api.{ Configuration, Logging }

trait MyService {
  def print(): Unit
}

object MyObj extends MyService {
  override def print(): Unit = println("MY obj inst")
}

@Singleton
class MyServiceImpl @Inject() (config: Configuration) extends MyService with Logging {
  override def print(): Unit =
    logger.info("Hi my proj: " + config.get[String]("service.name"))
}
