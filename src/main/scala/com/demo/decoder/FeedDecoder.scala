package com.demo.decoder

import akka.actor.{Actor, ActorRef, Props}
import com.demo.events.CarMessage
import com.demo.processor.FeedProcessor.ProcessCarFeed
import spray.json._

object FeedDecoder {
  def props(processor: ActorRef): Props = {
    Props(new FeedDecoder(processor))
  }
  case class Decode(value: String)
}
class FeedDecoder(processor: ActorRef) extends Actor with JsonProtocol {
  import FeedDecoder._

  override def receive = {
    case Decode(value) =>
     val carEvent = decode(value)
      println("Decoder: " + carEvent)
      processor ! ProcessCarFeed(carEvent)

  }

  def decode(value: String): CarMessage = {
    val event = value.parseJson.convertTo[CarMessage]
    event
  }


}
