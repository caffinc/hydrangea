package com.caffinc.hydrangea.core.filter

/**
  * Trait to mixin for filtering messages in stream
  *
  * @author Sriram
  */
trait Filter[T] {
  /**
    * Returns if the record must be filtered or not
    *
    * @param record Record to filter
    * @return true if record is good, false if it must be removed
    */
  def filter(record: T): Boolean
}
