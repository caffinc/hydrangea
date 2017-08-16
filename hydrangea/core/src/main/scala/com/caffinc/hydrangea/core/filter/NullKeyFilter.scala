package com.caffinc.hydrangea.core.filter

import com.caffinc.hydrangea.core.serde.KafkaRecord
import com.typesafe.scalalogging.LazyLogging

/**
  * Filters records with null Keys
  *
  * @author Sriram
  */
object NullKeyFilter extends Filter[KafkaRecord] with LazyLogging {
  def apply(implicit record: KafkaRecord): Boolean = filter

  override def filter(implicit record: KafkaRecord): Boolean = {
    if (null != record.key) {
      true
    } else {
      logger.info("Filtering: {}", record)
      false
    }
  }
}
