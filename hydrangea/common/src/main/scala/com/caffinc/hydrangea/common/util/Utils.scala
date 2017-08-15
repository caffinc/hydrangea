package com.caffinc.hydrangea.common.util

object Utils {
  def timeIt[A](task: => A): A = {
    val startTime = System.nanoTime()
    val res = task
    println(s"Took ${(System.nanoTime() - startTime)/1000000.0} milliseconds")
    res
  }
}
