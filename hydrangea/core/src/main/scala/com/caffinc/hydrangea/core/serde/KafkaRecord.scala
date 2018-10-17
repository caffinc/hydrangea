package com.caffinc.hydrangea.core.serde

import org.json4s.JObject

/**
  * Represents one record in the Kafka Queue
  *
  * @param topic      Topic this record belongs to
  * @param recordType Defines the type of record, useful for identifying the schema of the value
  * @param timestamp  Timestamp of the record
  * @param key        Key of the record
  * @param value      Value of the record
  */
case class KafkaRecord(topic: String, recordType: String, timestamp: Long, key: String, value: JObject)
