package com.caffinc.hydrangea.core.app

import java.util.regex.Pattern

import com.caffinc.hydrangea.core.filter.FilterNullKey
import com.caffinc.hydrangea.core.serde.KafkaRecord
import com.caffinc.hydrangea.core.transformer.{PrepareRecord, ShaveHeaders, StoreRecord}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.SubscribePattern
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Milliseconds, StreamingContext}
import org.json4s._

import scala.collection.JavaConverters._

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
    // Pre-process the data, remove unwanted stuff
    val preprocessedStream = stream
      .filter(FilterNullKey(_))
      .map(ShaveHeaders(_))
    // Apply any other transformations here, none at the moment
    val processedStream = process(preprocessedStream)

    // Store results
    val postprocessedStream = processedStream.map(StoreRecord(_))
      .map { case (topic, recordType, id) => (topic, recordType) }
      .countByValue()

    // Trigger actual computation
    postprocessedStream.foreachRDD(
      (rdd, time) => {
        val processedRecordCounts = rdd.collect()
        if (processedRecordCounts.length > 0)
          logger.info(
            "\n====================================\n" +
              "Time: {}\n====================================\n" +
              "{}", time, processedRecordCounts.mkString("\n"))
      })
    ssc.start()
    ssc.awaitTermination()
  }

  /**
    * Kafka topic patterns to subscribe to
    *
    * @return Hydrangea topic pattern
    */
  def pattern(implicit appConfig: Config): Pattern = appConfig.getString("kafka.topics.pattern").r.pattern

  /**
    * Builds the InputDStream and transforms the data into KafkaRecords
    *
    * @return SteamingSparkContext and DStream tuple
    */
  def getStream: (StreamingContext, DStream[KafkaRecord]) = {
    implicit val appConfig: Config = ConfigFactory.load()

    val sparkConfig = appConfig.getConfig("spark")
    val conf = new SparkConf()
      .setAppName(sparkConfig.getString("conf.appname"))
      .setMaster(sparkConfig.getString("conf.master"))
    val ssc = new StreamingContext(conf, Milliseconds(sparkConfig.getString("stream.interval.ms").toInt))
    logger.info("Starting Spark with the following config:\n{}", sparkConfig)

    val kafkaConfig = appConfig.getConfig("kafka.consumer").entrySet.asScala
      .map(kv => (kv.getKey, kv.getValue.unwrapped)).toMap

    logger.info("Connecting to Kafka with following config:\n{}", kafkaConfig)

    val stream = KafkaUtils.createDirectStream[String, JObject](
      ssc,
      PreferConsistent,
      SubscribePattern[String, JObject](pattern, kafkaConfig)
    )
    (ssc, stream.map(PrepareRecord(_)))
  }

  /**
    * Adds the custom filter and transformation scripts to the stream
    *
    * @param preprocess Pre-processed stream
    */
  def process(preprocess: DStream[KafkaRecord]): DStream[KafkaRecord] = {
    preprocess
  }
}
