package com.caffinc.hydrangea.core.transformer

import com.caffinc.hydrangea.core.serde.KafkaRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.json4s.JsonAST.JValue

/**
  * Transforms a ConsumerRecord[String, JValue] into a KafkaRecord. This transformer is used before
  * all other processes.
  *
  * @author Sriram
  */
object RecordTransformer extends Transformer[ConsumerRecord[String, JValue], KafkaRecord] {
  def apply(implicit record: ConsumerRecord[String, JValue]): KafkaRecord = transform

  override def transform(implicit record: ConsumerRecord[String, JValue]): KafkaRecord = {
    KafkaRecord(record.topic(), record.timestamp(), record.key(), record.value())
  }
}
