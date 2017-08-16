package com.caffinc.hydrangea.core.app

import java.util.Map.Entry

import com.typesafe.config.{Config, ConfigFactory, ConfigValue}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Milliseconds, StreamingContext}

/**
  * Starts the Core project to process the incoming messages
  *
  * @author Sriram
  */
object StartCore extends LazyLogging {
  val config: Config = ConfigFactory.load()

  def main(args: Array[String]): Unit = {
    val sparkConfig = config.getConfig("spark")
    val conf = new SparkConf()
      .setAppName(sparkConfig.getString("conf.appname"))
      .setMaster(sparkConfig.getString("conf.master"))
    val ssc = new StreamingContext(conf, Milliseconds(sparkConfig.getString("stream.interval.ms").toInt))
    logger.info("Starting Spark with the following config:\n{}", sparkConfig)

    val kafkaConfig = config.getConfig("kafka.consumer").entrySet()
      .toArray(new Array[Entry[String, ConfigValue]](0))
      .map(kv => (kv.getKey, kv.getValue.unwrapped())).toMap
    logger.info("Connecting to Kafka with following config:\n{}", kafkaConfig)

    val topics = Array("abc", "hydrangea", "a")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaConfig)
    )

    logger.info("Processing Kafka stream")
    stream.map(record => {
      logger.info("\n" * 10 + "Topic: {}\nRecord: {}", record.topic(), record.value())
      record.timestamp()
    }).print()
    ssc.start()
    ssc.awaitTermination()
  }
}
