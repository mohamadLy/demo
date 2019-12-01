package com.demo.db.entities

case class Car(registration: String, owner: String, subscription: String, color: String)
case class Trip(registration: String, time: String, speed: String)
case class CarResult(total: Long, cars: List[Car])
case class TripResult(total: Long, trips: List[Trip])
