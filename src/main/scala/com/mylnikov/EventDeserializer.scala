package com.mylnikov

import java.util

import org.codehaus.jackson.map.ObjectMapper
import org.apache.kafka.common.serialization.Deserializer

import scala.util.{Failure, Success}

/**
  * Deserializer for [[BookingEvent]]
  * Serialized input should be valid json representation of object.
  */
class EventDeserializer extends Deserializer[BookingEvent] {

  val objectMapper = new ObjectMapper()

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
  }

  override def deserialize(topic: String, data: Array[Byte]): BookingEvent = {
    try objectMapper.readValue(data, classOf[BookingEvent]) match {
      case event : BookingEvent => event
      case _ => null
    }
  }

  override def close(): Unit = {

  }
}
