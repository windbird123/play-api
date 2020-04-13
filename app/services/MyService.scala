package services

import javax.inject.Singleton

trait MyService {
  def print(): Unit
}

object MyObj extends MyService {
  override def print(): Unit = println("MY obj $$$$$$$$$$")
}

@Singleton
class MyServiceImpl extends MyService {
  override def print(): Unit = println("Hi my proj")
}
