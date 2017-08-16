package com.caffinc.hydrangea.web.services

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import play.api.libs.json.JsObject

object KafkaStreamer {
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")

  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  def streamToKafka(stream: String)(data: JsObject): Unit = {
    println(s"Streaming ${data.toString()} to $stream")
    producer.send(new ProducerRecord[String, String](stream, data.toString()))
    producer.flush()
  }
}
