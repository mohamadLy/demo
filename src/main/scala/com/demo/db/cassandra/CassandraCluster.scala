package com.demo.db.cassandra

import com.datastax.driver.core.{Cluster, HostDistance, PoolingOptions}

trait CassandraCluster extends CassandraConf {

  lazy val poolingOptions = {
    new PoolingOptions()
      .setConnectionsPerHost(HostDistance.LOCAL, 4, 10)
      .setConnectionsPerHost(HostDistance.REMOTE, 2, 4)
  }

  lazy val cluster: Cluster = {
    val builder = Cluster.builder()
    for (cp <- cassandraHosts) {
      builder.addContactPoint(cp)
    }
    builder.withPort(cassandraPort)
    builder.withPoolingOptions(poolingOptions)

    builder.build()
  }

  lazy implicit val session = cluster.connect()
}
