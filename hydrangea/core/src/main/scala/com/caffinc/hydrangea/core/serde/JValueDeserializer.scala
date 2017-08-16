package com.caffinc.hydrangea.core.serde

import java.util

import org.apache.kafka.common.serialization.Deserializer
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  * Parses incoming Kafka message to JValue
  *
  * @author Sriram
  */
class JValueDeserializer extends Deserializer[JValue] {
  private var encoding: String = "UTF8"

  override def configure(configs: util.Map[String, _], isKey: Boolean) = {
    val propertyName: String = if (isKey) "key.deserializer.encoding" else "value.deserializer.encoding"
    var encodingValue: Any = configs.get(propertyName)
    if (encodingValue == null) encodingValue = configs.get("deserializer.encoding")
    if (encodingValue != null && encodingValue.isInstanceOf[String]) encoding = encodingValue.asInstanceOf[String]
  }

  override def deserialize(topic: String, data: Array[Byte]) = {
    if (data == null)
      null
    else
      parse(new String(data, encoding))
  }

  override def close() = {}
}
