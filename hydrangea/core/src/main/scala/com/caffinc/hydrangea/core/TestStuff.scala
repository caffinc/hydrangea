package com.caffinc.hydrangea.core

import java.util
import java.util.Properties
import java.util.concurrent.atomic.AtomicBoolean

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object TestStuff extends App {
  val x = List((1, 2), (1, 3), (2, 3), (2, 4))
  println(x.groupBy(_._1).foldLeft(List[(Int, Int)]()) {
    case (g, (id, l)) => {
      val l0 = l.foldLeft(0) { case (b, (_, v)) => b + v }
      (id, l0) :: g
    }
  }.toMap)

  implicit val appConfig: Config = ConfigFactory.load()

  val kafkaConfig = appConfig.getConfig("kafka.consumer").entrySet.asScala
    .map(kv => (kv.getKey, kv.getValue.unwrapped)).toMap

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("group.id", "consumer-tutorial2")
  props.put("key.deserializer", classOf[StringDeserializer].getName)
  props.put("value.deserializer", classOf[StringDeserializer].getName)

  val producer = {
    import org.apache.kafka.clients.producer.ProducerConfig
    val props = new Properties
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-tutorial")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    new KafkaProducer[String, String](props)
  }
  (0 until 100).foreach { i =>
    producer.send(new ProducerRecord[String, String]("test", "Dummy " + i))
  }
  val running = new AtomicBoolean(true)
  (0 until 2).foreach { i =>
    Future {
      println("Starting " + i)
      val consumer = new KafkaConsumer[String, String](props, new StringDeserializer, new StringDeserializer)
      consumer.subscribe(util.Arrays.asList("test"))
      while (running.get()) {
        val records: ConsumerRecords[String, String] = consumer.poll(1000)
        records.asScala.toArray.foreach { record =>
          println(i + ": " + record.offset + ": " + record.value)
        }
      }
      consumer.close()
    } map {
      _ => println("Done")
    }
  }

  Thread.sleep(10000)

}
