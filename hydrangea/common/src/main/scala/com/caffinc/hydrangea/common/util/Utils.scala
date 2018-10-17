package com.caffinc.hydrangea.common.util

import com.typesafe.scalalogging.LazyLogging

/**
  * Utility methods
  *
  * @author Sriram
  */
object Utils extends LazyLogging {
  /**
    * Times a task
    *
    * @param task Task to time
    * @tparam A Return type of the task
    * @return result of the task
    */
  def timeIt[A](task: => A): A = {
    val startTime = System.nanoTime()
    val res = task
    logger.info("Took {} milliseconds", (System.nanoTime() - startTime) / 1000000.0)
    res
  }
}
