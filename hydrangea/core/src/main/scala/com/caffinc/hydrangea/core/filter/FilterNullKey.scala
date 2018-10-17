package com.caffinc.hydrangea.core.filter

import com.caffinc.hydrangea.core.serde.KafkaRecord
import com.typesafe.scalalogging.LazyLogging

/**
  * Filters records with null Keys
  *
  * @author Sriram
  */
object FilterNullKey extends Filter[KafkaRecord] with LazyLogging {
  def apply(record: KafkaRecord): Boolean = filter(record)

  override def filter(record: KafkaRecord): Boolean = {
    if (null != record.key) {
      true
    } else {
      logger.info("Filtering: {}", record)
      false
    }
  }
}
