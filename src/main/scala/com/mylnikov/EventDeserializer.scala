package com.mylnikov

import java.util

import org.codehaus.jackson.map.ObjectMapper
import org.apache.kafka.common.serialization.Deserializer


class EventDeserializer extends Deserializer[BookingEvent] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {

  }

  override def deserialize(topic: String, data: Array[Byte]): BookingEvent = {

    val mapper = new ObjectMapper()
    var event:BookingEvent = null
    try
      mapper.readValue(data, classOf[BookingEvent])
    catch {
      case e: Exception =>
        e.printStackTrace()
        null;
    }
  }

  override def close(): Unit = {

  }
}
