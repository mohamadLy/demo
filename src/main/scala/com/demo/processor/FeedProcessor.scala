package com.demo.processor

import akka.actor.{Actor, ActorRef, Props}
import com.demo.events.CarMessage
import com.redis._
import com.demo.db.entities.{Car, Trip}
import com.demo.writer.Writer.{GetCar, SaveTrip}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._


object FeedProcessor {
  val redisKey = "sapdemo"

  def props(writer: ActorRef) = Props(new FeedProcessor(writer))

  case class ProcessCarFeed(car: CarMessage)
  case class CarResponse(car: Car)

}
class FeedProcessor(writer: ActorRef) extends Actor {
  import FeedProcessor._

  implicit val timeout: Timeout = 5.seconds
  // should check if car belongs to subscribe customer
  // if belongs to subscribe customer just write it to the database
  // else generate a invoice and write it to the database

  override def receive: Receive = {
    case ProcessCarFeed(event) =>
      processCarFeed(event)
  }


  def processCarFeed(carEvent: CarMessage) = {
    // check if car is cached
    val redisClient = new RedisClient("0.0.0.0", 6379)
    if(redisClient.hexists(redisKey, carEvent.registrationNumber) == true) {
      val subscription = redisClient.hget(redisKey, carEvent.registrationNumber)
      println("processCarFeed: Car cached")
      if(!subscription.toString.equalsIgnoreCase("Paid")) {
        // generate invoice
      }
    } else {
      println("processCarFeed: Car not cached")
      // key is not cached
      // get data from db
      val car = Await.result(writer ? GetCar(carEvent.registrationNumber, self), Duration.Inf).asInstanceOf[Car]


      // Cache it for next time
      val map = Iterable[Product2[String, String]] {
        (carEvent.registrationNumber, car.subscription)
      }
      redisClient.hmset(redisKey, map)
      redisClient.expire(redisKey, 60 * 5)

      if(!car.subscription.toString.equalsIgnoreCase("Paid")) {
        // generate invoice
      }
    }
    val trip = Trip(carEvent.registrationNumber, carEvent.time, carEvent.speed)
    writer ! SaveTrip(trip)
  }
}
