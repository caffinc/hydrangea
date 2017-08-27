package com.caffinc.hydrangea.web.helpers

import play.api.libs.json.{JsObject, Json}

/**
  * Unified API messaging helper
  *
  * @author Sriram
  */
object Messaging {
  /**
    * Returns a JsObject containing a success message
    *
    * @param message Message to send
    * @return JsObject containing the message
    */
  def getMessage(message: String): JsObject = {
    Json.obj("message" -> message)
  }

  /**
    * Returns a JsObject containing an error message
    *
    * @param error Error message to send
    * @return JsObject containing the error
    */
  def getError(error: String): JsObject = {
    Json.obj("error" -> error)
  }
}
