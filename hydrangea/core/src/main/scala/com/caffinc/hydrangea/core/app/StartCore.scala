package com.caffinc.hydrangea.core.app

import java.util.Map.Entry

import com.caffinc.hydrangea.common.util.Utils._
import com.typesafe.config.{ConfigFactory, ConfigValue}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Starts the Core project to process the incoming messages
  *
  * @author Sriram
  */
object StartCore extends LazyLogging {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load().getConfig("kafka.consumer").entrySet().toArray(new Array[Entry[String, ConfigValue]](0))
      .map(kv => (kv.getKey, kv.getValue.unwrapped())).toMap
    logger.info("Connecting to Kafka with following config:\n{}", config)
    val conf = new SparkConf().setAppName("Hydrangea").setMaster("local[*]")

    val ssc = new StreamingContext(conf, Seconds(1))
    timeIt {
      val topics = Array("abc", "hydrangea", "a")
      val stream = KafkaUtils.createDirectStream[String, String](
        ssc,
        PreferConsistent,
        Subscribe[String, String](topics, config)
      )
      stream.start()
      stream.map(record =>
        logger.info("\n" * 10 + "Topic: {}\nRecord: {}", record.topic(), record.value())).print()
      ssc.awaitTermination()
    }
  }
}
