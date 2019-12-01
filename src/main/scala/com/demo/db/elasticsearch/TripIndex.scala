package com.demo.db.elasticsearch

import com.demo.db.entities.{Trip, TripResult}
import com.sksamuel.elastic4s.http.{ElasticProperties, HttpClient}
import com.typesafe.scalalogging.LazyLogging
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.sprayjson._

class TripIndex extends ESClient with ESJsonProtocol with LazyLogging {

  val indexType = "car_trips_event_doc"
  def indexName = "car_trips"

  def addIndex(name: String) = {
    try {
      client.execute {
        createIndex(name) mappings (
          mapping(indexType) as (
            keywordField("registration"),
            keywordField("time"),
            keywordField("speed"),
          ))
      }
      logger.info("Created index {} for elasticsearch" + name)
    } catch {
      case e =>
        logger.error("Failed to create index" + e)
    }
  }

  def initIndices = {
    logger.info("Initializing trip event indices ...")

    val name = indexName
    logger.info("index name" + name)
    addIndex(name)
  }


  def storeTrip(trip: Trip) = {
    client.execute {
      indexInto(indexName / indexType).doc(trip).id(trip.registration).refreshImmediately
    }
  }

  def getTrip(registration: String): TripResult = {
    val response =
      client.execute {
        search(indexName / indexType).query(registration)
      }.await
    //response.result.hits.hits.head.sourceAsString.parseJson.convertTo[Car]
    response.map {f => TripResult(f.totalHits, f.to[Trip].toList)}.result
  }

}
