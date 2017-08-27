package com.caffinc.hydrangea.web.services

import java.util.{Properties, UUID}

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import play.api.libs.json.JsObject

import scala.util.Try

/**
  * Writes data to Kafka
  *
  * @author Sriram
  */
object KafkaStreamer extends LazyLogging {
  private lazy val producer: KafkaProducer[String, String] = {
    logger.info("Loading KafkaProducer config")
    val props = new Properties()
    ConfigFactory.load().getConfig("kafka.producer").entrySet()
      .forEach(kv => props.put(kv.getKey, kv.getValue.unwrapped()))
    logger.info("Connecting to Kafka with following config:\n{}", props)
    new KafkaProducer[String, String](props)
  }

  def streamToKafka(stream: String)(data: JsObject): Try[Unit] = Try {
    logger.debug("Streaming {} to {}", data, stream)
    val topic = "hydrangea_" + stream
    producer.send(new ProducerRecord[String, String](topic, UUID.randomUUID().toString, data.toString))
  }
}
