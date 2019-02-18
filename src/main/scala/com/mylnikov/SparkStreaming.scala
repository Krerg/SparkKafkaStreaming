package com.mylnikov

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming._
import org.apache.spark.SparkConf
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils

/**
  * Entry point for streaming job.
  * Saves entities from kafka topic to hdfs root's folder.
  */
object SparkStreaming {

  /**
    * For testing purposes.
    */
  var ssc: StreamingContext = _

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      throw new IllegalArgumentException("You should specify bootstrap server and path in arguments")
    }

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> args(0),
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[EventDeserializer],
      "group.id" -> "kafkaStreaming"
    )

    val sparkConf = new SparkConf()
      .setAppName("KafkaStreaming")
      //comment this in case deployment
      .setMaster("local[*]")
    ssc = new StreamingContext(sparkConf, Milliseconds(200))

    val topics = Array("test")
    val stream = KafkaUtils.createDirectStream[String, BookingEvent](
      ssc,
      PreferConsistent,
      Subscribe[String, BookingEvent](topics, kafkaParams)
    )
    saveStreamToHdfs(stream, args(1))
    ssc.start()
    ssc.awaitTermination()
  }

  /**
    * Saves kafka stream to hdfs.
    *
    * @param stream kafka stream
    * @param path hdfs path where entities would be saved
    */
  def saveStreamToHdfs(stream: InputDStream[ConsumerRecord[String, BookingEvent]], path: String) : Unit = {
    stream.map(record =>  {
      record.value()
    }).saveAsTextFiles(s"$path/event", "")
  }

}
