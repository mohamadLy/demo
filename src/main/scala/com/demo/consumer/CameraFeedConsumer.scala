package com.demo.consumer

import scala.collection.JavaConverters._
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties

import akka.actor.{ActorRef}
import com.demo.decoder.FeedDecoder.Decode

class CameraFeedConsumer(decoder: ActorRef) {

  def consumeVideoFeed(topic: String) = {
    println("Consumer camera feed")

    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9093")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "latest")
    props.put("group.id", "consumer-group")

    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)

    consumer.subscribe(java.util.Arrays.asList(topic))
    while(true) {
      val record = consumer.poll(1000).asScala
      for(data <- record.iterator) {
        println(data.value())
        decoder ! Decode(data.value())
      }
    }
  }

}
