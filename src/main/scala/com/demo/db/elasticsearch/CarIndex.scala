package com.demo.db.elasticsearch

import com.demo.db.entities.{Car, CarResult}
import com.sksamuel.elastic4s.http.{ElasticProperties, HttpClient}
import com.typesafe.scalalogging.LazyLogging
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.sprayjson._
import spray.json._

class CarIndex extends LazyLogging with ESJsonProtocol with ESClient {

  val indexType = "car_event_doc"
  def indexName = "car_events"

  def addIndex(name: String) = {
    try {
      client.execute {
        createIndex(name) mappings (
          mapping(indexType) as (
            keywordField("registration"),
            keywordField("owner"),
            keywordField("subscription"),
            keywordField("color")
          ))
      }
      logger.info("Created index {} for elasticsearch" + name)
    } catch {
      case e =>
        logger.error("Failed to create index" + e)
    }
  }

  def initIndices = {
    logger.info("Initializing car event indices ...")

    val name = indexName
    logger.info("index name" + name)
    addIndex(name)
  }


  def storeCar(car: Car) = {
    client.execute {
     indexInto(indexName / indexType).doc(car).id(car.registration).refreshImmediately
    }
  }

  def getCar(registration: String): CarResult = {
    val response =
      client.execute {
      search(indexName / indexType).query(registration)
    }.await
    //response.result.hits.hits.head.sourceAsString.parseJson.convertTo[Car]
    println(response)
    response.map {f => CarResult(f.totalHits, f.to[Car].toList)}.result
  }

}
