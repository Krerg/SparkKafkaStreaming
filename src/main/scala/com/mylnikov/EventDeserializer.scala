package com.mylnikov

import java.io.EOFException
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

  val EMPTY_EVENT = new BookingEvent()

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
  }

  override def deserialize(topic: String, data: Array[Byte]): BookingEvent = {
    try {objectMapper.readValue(data, classOf[BookingEvent])}
    catch {
      case ex: Exception => EMPTY_EVENT
    }
  }

  override def close(): Unit = {

  }
}
