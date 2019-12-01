package com.demo.db

import com.demo.db.elasticsearch.{CarIndex, TripIndex}
import com.demo.db.entities.{Car, Trip}

object DB {

  val carIndex = new CarIndex
  val tripIndex = new TripIndex

  def initDB(): Unit = {
    carIndex.initIndices
    tripIndex.initIndices
  }

  def insertCar(car: Car) = {
    carIndex.storeCar(car)
  }

  def insertTrip(trip: Trip) = {
    tripIndex.storeTrip(trip)
  }

  def getCar(registration: String) = {
    val carResult = carIndex.getCar(registration)
    if(carResult.total != 0) {
      println("car= " + carResult.cars(0))
      carResult.cars(0)
    }
    else {
      println("No car" + carResult.cars(0))
      carResult.cars(0)
    }
  }

  def getTrip(registration: String) = {
    tripIndex.getTrip(registration).trips(0)
  }
}
