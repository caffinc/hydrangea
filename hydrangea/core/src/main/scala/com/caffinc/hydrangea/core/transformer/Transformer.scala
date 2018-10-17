package com.caffinc.hydrangea.core.transformer

import com.typesafe.config.{Config, ConfigFactory}
import org.json4s.DefaultFormats

/**
  * Trait to mixin for transforming message in stream
  *
  * @author Sriram
  */
trait Transformer[T, U] {
  implicit val formats: DefaultFormats = DefaultFormats
  lazy val transformerConfig: Config = ConfigFactory.load().getConfig("transformer")

  def transform(record: T): U
}
