package com.mylnikov

import com.fasterxml.jackson.databind.ObjectMapper
import org.scalatest.FunSuite

class EventDeserializerTest extends FunSuite{

  val eventDeserializer = new EventDeserializer()

  test("Should deserialize event") {
    val event = BookingEvent(siteName = 10)
    val objectMapper = new ObjectMapper()
    val serializedEvent = objectMapper.writeValueAsBytes(event)
    val deserializedEvent = eventDeserializer.deserialize("", serializedEvent)
    assert(event.siteName == deserializedEvent.siteName)
  }

}
