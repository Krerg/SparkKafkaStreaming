package com.mylnikov

import org.apache.spark.streaming._
import org.apache.spark.SparkConf
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
object Main {

  def main(args: Array[String]): Unit = {

    if (args.length < 1) {
      throw new IllegalArgumentException("You should specify bootstrap server in arguments")
    }

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> args(0),
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[EventDeserializer],
      "group.id" -> "groupid",
      "auto.offset.reset" -> "latest"
    )

    val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(1))

    val topics = Array("test")
    val stream = KafkaUtils.createDirectStream[String, BookingEvent](
      ssc,
      PreferConsistent,
      Subscribe[String, BookingEvent](topics, kafkaParams)
    )

    stream.map(record => record.value()).saveAsTextFiles("/user/root/event", "")

    ssc.start()
    ssc.awaitTermination()

  }

}
