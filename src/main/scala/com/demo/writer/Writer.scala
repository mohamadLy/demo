package com.demo.writer

import akka.actor.{Actor, ActorRef, Props}
import com.demo.db.DB
import com.demo.db.entities.{Car, Trip}
import com.demo.processor.FeedProcessor.CarResponse
import com.typesafe.scalalogging.LazyLogging

object Writer {
  def props() = Props(new Writer)

  case class SaveTrip(trip: Trip)
  case class SaveCar(car: Car)
  case class GetCar(registrationNumber: String, replyTo: ActorRef)

}

class Writer extends Actor with LazyLogging {
  import Writer._

  override def receive: Receive = {
    case SaveTrip(trip) =>
      logger.info("Received SaveTrip {}", trip)
     DB.insertTrip(trip)
    case SaveCar(car) =>
      logger.info("Received SaveCar {}", car)
      DB.insertCar(car)
    case GetCar(registrationNumber, replyTo) =>
      logger.info("Received GetCar {}", registrationNumber)
      val car = DB.getCar(registrationNumber)
      replyTo ! CarResponse(car)
  }
}
