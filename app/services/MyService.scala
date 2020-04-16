package services

import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Logging, MarkerContext}
import zio.{Has, RIO, ZIO}

trait MyService {
  def print(): RIO[Has[MarkerContext], Unit]
}

// impl
@Singleton
class MyServiceImpl @Inject() (config: Configuration) extends MyService with Logging {
  override def print(): RIO[Has[MarkerContext], Unit] = ZIO.access { implicit env =>
  // marker logging 울 위해 필요함
  import libs.playzio._
    logger.info("Hi my serviceImpl: " + config.get[String]("service.name"))
  }
}
