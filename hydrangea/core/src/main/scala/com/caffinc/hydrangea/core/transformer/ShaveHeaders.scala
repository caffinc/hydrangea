package com.caffinc.hydrangea.core.transformer

import com.caffinc.hydrangea.common.constants.MessageFields._
import com.caffinc.hydrangea.core.serde.KafkaRecord
import com.typesafe.scalalogging.LazyLogging
import org.json4s.JsonDSL._
import org.json4s._

import scala.collection.JavaConverters._

/**
  * Removes unnecessary headers from the record
  *
  * @author Sriram
  */
object ShaveHeaders extends Transformer[KafkaRecord, KafkaRecord] with LazyLogging {
  def apply(record: KafkaRecord): KafkaRecord = transform(record)

  override def transform(record: KafkaRecord): KafkaRecord = {
    record.copy(value = shaveHeaders(record.value))
  }

  lazy val invalidHeaders: Set[String] = transformerConfig.getStringList("shaveheaders.invalid").asScala.toSet

  def shaveHeaders(value: JObject): JObject = {
    def validHeader(key: String): Boolean = !(invalidHeaders contains key.toLowerCase())

    def removeInvalidHeaders(headers: JValue): JValue = for {
      JObject(child) <- headers
      JField(key, values) <- child if validHeader(key)
    } yield (key, values)

    value ~ (FIELD_HEADERS -> removeInvalidHeaders(value \ FIELD_HEADERS))
  }
}
