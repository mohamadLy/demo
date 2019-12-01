package com.demo.db.elasticsearch

import com.sksamuel.elastic4s.http.{ElasticProperties, Response}

trait ESClient {
  val client = com.sksamuel.elastic4s.http.ElasticClient(ElasticProperties("http://localhost:9200"))
}
