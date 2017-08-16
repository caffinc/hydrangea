package com.caffinc.hydrangea.core.app

import java.util.Map.Entry

import com.caffinc.hydrangea.core.filter.NullKeyFilter
import com.caffinc.hydrangea.core.serde.KafkaRecord
import com.caffinc.hydrangea.core.transformer.{HeaderTransformer, RecordTransformer, StorageTransformer}
import com.typesafe.config.{Config, ConfigFactory, ConfigValue}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Milliseconds, StreamingContext}
import org.json4s._

/**
  * Starts the Core project to process the incoming messages
  *
  * @author Sriram
  */
object StartCore extends LazyLogging {
  /**
    * Main method to run it all
    *
    * @param args Command line arguments (unused)
    */
  def main(args: Array[String]): Unit = {
    val (ssc, stream) = getStream
    logger.info("Processing Kafka stream")
    stream
      .filter(NullKeyFilter(_))
      .map(HeaderTransformer(_))
      .map(StorageTransformer(_))
      .print()
    ssc.start()
    ssc.awaitTermination()
  }

  /**
    * Kafka topics to subscribe to
    *
    * @return Array of topics
    */
  def topics: Array[String] = Array("abc", "hydrangea", "a")

  /**
    * Builds the InputDStream and transforms the data into KafkaRecords
    *
    * @return SteamingSparkContext and DStream tuple
    */
  def getStream: (StreamingContext, DStream[KafkaRecord]) = {
    val appConfig: Config = ConfigFactory.load()
    val sparkConfig = appConfig.getConfig("spark")
    val conf = new SparkConf()
      .setAppName(sparkConfig.getString("conf.appname"))
      .setMaster(sparkConfig.getString("conf.master"))
    val ssc = new StreamingContext(conf, Milliseconds(sparkConfig.getString("stream.interval.ms").toInt))
    logger.info("Starting Spark with the following config:\n{}", sparkConfig)

    val kafkaConfig = appConfig.getConfig("kafka.consumer").entrySet()
      .toArray(new Array[Entry[String, ConfigValue]](0))
      .map(kv => (kv.getKey, kv.getValue.unwrapped())).toMap
    logger.info("Connecting to Kafka with following config:\n{}", kafkaConfig)

    val stream = KafkaUtils.createDirectStream[String, JValue](
      ssc,
      PreferConsistent,
      Subscribe[String, JValue](topics, kafkaConfig)
    )
    (ssc, stream.map(RecordTransformer(_)))
  }
}
