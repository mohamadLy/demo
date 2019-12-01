package com.demo.db

import com.demo.db.cassandra.CassandraCluster
import com.demo.db.entities.{Car, Trip}

object DemoDB {
  // create car table
  lazy val insertCarSchema =
    """
      |INSERT INTO sapdemo.car(registration, owner, subscription, color)
      |VALUES(?, ?, ?, ?)
    """.stripMargin

  lazy val insertTripSchema =
    """
      |INSERT INTO sapdemo.trip(registration, time, speed)
      |VALUES(?, ?, ?)
    """.stripMargin

  // get Car
  lazy val getCarSchema =
    """
      |SELECT * FROM sapdemo.car
      |WHERE registration = ?
      |LIMIT 1
    """.stripMargin
  // get trip
  lazy val getTripSchema =
    """
      |SELECT * FROM sapdemo.trip
      |WHERE registration = ?
      |LIMIT 1
    """.stripMargin
}

class DemoDB extends CassandraCluster {
  import DemoDB._

  lazy val insertCarStatement = session.prepare(insertCarSchema)
  lazy val insertTripStatement = session.prepare(insertTripSchema)
  lazy val getCarStatement = session.prepare(getCarSchema)
  lazy val getTripStatement = session.prepare(getTripSchema)


  def insertCar(car: Car) = {
    val statement = insertCarStatement.bind()
    statement.setString("registration", car.registration)
    statement.setString("owner", car.owner)
    statement.setString("subscription", car.subscription)
    statement.setString("color", car.color)

    session.execute(statement)
  }

  def insertTrip(trips: Trip) = {
    val statement = insertTripStatement.bind()
    statement.setString("registration", trips.registration)
    statement.setString("time", trips.time)
    statement.setString("speed", trips.speed)

    session.execute(statement)
  }


  def getCar(registrationNumber: String) = {
    val row = session.execute(getCarStatement.bind(registrationNumber)).one()
    if(row != null) {
      Option(
        Car(
          row.getString("registration"),
          row.getString("owner"),
          row.getString("subscription"),
          row.getString("color"))
      )
    } else None
  }

  def getTrip(registrationNumber: String) = {
    val row = session.execute(getTripStatement.bind(registrationNumber)).one()
    if(row != null) {
      Option(
        Trip(
          row.getString("registration"),
          row.getString("time"),
          row.getString("speed"))
      )
    } else None
  }
}
