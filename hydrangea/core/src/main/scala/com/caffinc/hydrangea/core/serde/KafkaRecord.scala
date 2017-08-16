package com.caffinc.hydrangea.core.serde

import org.json4s.JValue

/**
  * Represents one record in the Kafka Queue
  *
  * @param topic     Topic this record belongs to
  * @param timestamp Timestamp of the record
  * @param key       Key of the record
  * @param value     Value of the record
  */
case class KafkaRecord(topic: String, timestamp: Long, key: String, value: JValue)
