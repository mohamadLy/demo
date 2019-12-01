package com.demo.db.elasticsearch

import com.demo.db.entities.{Car, CarResult, Trip, TripResult}
import spray.json.DefaultJsonProtocol

trait ESJsonProtocol extends DefaultJsonProtocol {

  implicit val carFormat = jsonFormat4(Car)
  implicit val carResultFormat = jsonFormat2(CarResult)
  implicit val tripFormat= jsonFormat3(Trip)
  implicit val tripResultFormat = jsonFormat2(TripResult)

}
