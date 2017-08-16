package com.caffinc.hydrangea.core.transformer

import com.caffinc.hydrangea.core.serde.KafkaRecord

/**
  * Stores the KafkaRecord into a persistent storage (MongoDB)
  *
  * @author Sriram
  */
object StorageTransformer extends Transformer[KafkaRecord, String] {
  def apply(implicit record: KafkaRecord): String = transform

  override def transform(implicit record: KafkaRecord): String = {
    // TODO: Store into MongoDB
    record.key
  }
}