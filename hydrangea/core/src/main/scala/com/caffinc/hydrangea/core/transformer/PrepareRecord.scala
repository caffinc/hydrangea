package com.caffinc.hydrangea.core.transformer

import com.caffinc.hydrangea.common.constants.MessageFields.FIELD_REQUEST_BODY
import com.caffinc.hydrangea.core.serde.KafkaRecord
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.json4s.JObject

/**
  * Transforms a ConsumerRecord[String, JValue] into a KafkaRecord. This transformer is used before
  * all other processes.
  *
  * @author Sriram
  */
object PrepareRecord extends Transformer[ConsumerRecord[String, JObject], KafkaRecord] with LazyLogging {
  def apply(record: ConsumerRecord[String, JObject]): KafkaRecord = transform(record)

  private val defaultType: String = transformerConfig.getString("preparerecord.defaulttype")

  override def transform(record: ConsumerRecord[String, JObject]): KafkaRecord = {
    val recordType = (record.value() \ FIELD_REQUEST_BODY \ "recordType").extractOrElse[String](defaultType)
    logger.debug("Record: {}", record)
    KafkaRecord(record.topic(), recordType, record.timestamp(), record.key(), record.value())
  }
}
