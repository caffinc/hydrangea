package com.caffinc.hydrangea.core.transformer

/**
  * Trait to mixin for transforming message in stream
  *
  * @author Sriram
  */
trait Transformer[T, U] {
  def transform(implicit record: T): U
}
