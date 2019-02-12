import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

object Main {

  def main(args: Array[String]): Unit = {
    val props = new Properties()

    props.put("bootstrap.servers", "localhost:6667")

    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")



    props.put("enable.auto.commit", "true")

     props.put("auto.commit.interval.ms", "1000")

    props.put("auto.offset.reset", "earliest")

      props.put("session.timeout.ms", "30000")

    val topic = "test"

     val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)

     consumer.subscribe(Collections.singletonList(topic))

    while (true) {
      val records: ConsumerRecords[String, String] = consumer.poll(100)
      println(records.isEmpty)
    }
  }

}
