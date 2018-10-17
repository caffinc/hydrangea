package com.caffinc.hydrangea.core.serde

import java.util

import org.apache.kafka.common.serialization.Deserializer
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  * Parses incoming Kafka message to JObject
  *
  * @author Sriram
  */
class JObjectDeserializer extends Deserializer[JObject] {
  private var encoding: String = "UTF8"

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
    val propertyName: String = if (isKey) "key.deserializer.encoding" else "value.deserializer.encoding"
    val encodingValue = {
      val propertyValue = configs.get(propertyName)
      if (propertyValue == null) configs.get("deserializer.encoding") else propertyValue
    }
    if (encodingValue != null && encodingValue.isInstanceOf[String]) encoding = encodingValue.asInstanceOf[String]
  }

  override def deserialize(topic: String, data: Array[Byte]): JObject = {
    if (data == null)
      null
    else
      parse(new String(data, encoding)).asInstanceOf[JObject]
  }

  override def close(): Unit = {}
}
