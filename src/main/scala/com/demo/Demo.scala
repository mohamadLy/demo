package com.demo

import akka.actor.ActorSystem
import com.demo.consumer.CameraFeedConsumer
import com.demo.db.DB
import com.demo.db.entities.Car
import com.demo.decoder.FeedDecoder
import com.demo.processor.FeedProcessor
import com.demo.writer.Writer

object Demo extends App {

  val topic = "videoFeedTopic"

  val system = ActorSystem("ciscodemo-receiver")
  DB.initDB()

  // Test data
  DB.insertCar(Car("test740", "John Doe", "subscription", "red"))

  val writer = system.actorOf(Writer.props)
  val processor = system.actorOf(FeedProcessor.props(writer), "processor")
  val decoder = system.actorOf(FeedDecoder.props(processor), "decoder")
  val consumer = new CameraFeedConsumer(decoder)

  consumer.consumeVideoFeed(topic)

}