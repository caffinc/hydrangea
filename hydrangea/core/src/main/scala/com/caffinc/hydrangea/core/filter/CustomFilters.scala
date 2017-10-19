package com.caffinc.hydrangea.core.filter

import com.caffinc.hydrangea.core.serde.KafkaRecord
import com.typesafe.config.ConfigFactory
import groovy.util.GroovyScriptEngine

import scala.collection.JavaConverters._

class CustomFilters extends Filter[KafkaRecord] {
  private val filterConfig = ConfigFactory.load().getConfig("custom.filters")
  private val filterPath = filterConfig.getString("path")
  private val groovyScriptEngine = new GroovyScriptEngine(filterPath)
  private val filters = filterConfig.getConfigList("filterscripts").asScala.map { config =>
    val detector = config.getObject("detector")
    val filter = config.getString("filter")
    (detector, filter)
  }

  /**
    * Returns if the record must be filtered or not
    *
    * @param record Record to filter
    * @return true if record is good, false if it must be removed
    */
  override def filter(record: KafkaRecord): Boolean = {
    //    filters.foreach {
    //      if (matched(record)) {
    //        val binding = new Binding()
    //        binding.setProperty("data", record.value)
    //      }

    return true
    //    }
  }

  private def matched(record: KafkaRecord): Boolean = {
    true
  }

}
