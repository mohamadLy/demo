package com.demo.decoder

import com.demo.events.{CarMessage}
import spray.json.DefaultJsonProtocol

trait JsonProtocol extends DefaultJsonProtocol{
  implicit val carFormat = jsonFormat4(CarMessage)
}
