package com.demo.db.cassandra

import com.typesafe.config.ConfigFactory

trait CassandraConf {
  val cassandraConf = ConfigFactory.load("cassandra.conf")

  lazy val cassandraKeyspace = cassandraConf.getString("cassandra.keysapce")
  lazy val cassandraHosts = cassandraConf.getString("cassandra.hosts").split(",").map(_.trim)
  lazy val cassandraPort = cassandraConf.getInt("cassandra.port")
}
