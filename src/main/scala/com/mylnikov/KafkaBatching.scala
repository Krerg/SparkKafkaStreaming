package com.mylnikov

import java.nio.charset.StandardCharsets
import java.nio.file._
import java.time.Duration
import java.util
import java.util.Properties
import java.util.function.Consumer

import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

/**
  *
  */
object KafkaBatching {

  var stopped = false

  def main(args: Array[String]): Unit = {
    val properties = new Properties()
    properties.put("bootstrap.servers", "localhost:9092")
    properties.put("group.id", "kafka-poll")
    properties.put("auto.offset.reset", "latest")
    properties.put("key.deserializer", classOf[StringDeserializer])
    properties.put("value.deserializer", classOf[StringDeserializer])

    val kafkaConsumer = new KafkaConsumer[String, String](properties)
    kafkaConsumer.subscribe(util.Arrays.asList("test"))

    var counterPart = 0
    while (!Thread.interrupted() && !stopped) {
      val results = kafkaConsumer.poll(Duration.ofSeconds(30))
      counterPart+=1
      val output = Paths.get(s"tmp\\event-part$counterPart.txt")
      output.toFile.createNewFile()
      results.iterator().forEachRemaining(new Consumer[ConsumerRecord[String, String]]() {
        override def accept(record: ConsumerRecord[String, String]): Unit = {
          Files.write(output, record.value().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND)
        }
      })
    }

  }

}
