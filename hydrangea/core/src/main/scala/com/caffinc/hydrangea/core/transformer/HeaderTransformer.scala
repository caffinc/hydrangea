package com.caffinc.hydrangea.core.transformer

import com.caffinc.hydrangea.core.serde.KafkaRecord
import com.typesafe.scalalogging.LazyLogging

/**
  * Removes unnecessary headers from the record
  *
  * @author Sriram
  */
object HeaderTransformer extends Transformer[KafkaRecord, KafkaRecord] with LazyLogging {
  def apply(implicit record: KafkaRecord): KafkaRecord = transform

  override def transform(implicit record: KafkaRecord): KafkaRecord = {
    logger.info("Record: {}", record)
    // TODO: Remove unnecessary headers
    record
  }
}
