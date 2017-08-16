package com.caffinc.hydrangea.web.controllers

import com.caffinc.hydrangea.web.services.KafkaStreamer._
import play.api.libs.json.Json
import play.api.mvc.InjectedController

class Devour extends InjectedController {
  def devourPost(stream: String) = Action { request =>
    streamToKafka(stream) {
      request.body.asJson match {
        case None =>
          Json.obj("_headers" -> request.headers.toMap, "_query" -> request.queryString)
        case Some(data) =>
          Json.obj("_headers" -> request.headers.toMap, "_query" -> request.queryString, "_body" -> data)
      }
    }
    Ok
  }

  def devourGet(stream: String) = Action { request =>
    streamToKafka(stream) {
      Json.obj("_headers" -> request.headers.toMap, "_query" -> request.queryString)
    }
    Ok
  }

  def devourDefault() = Action {
    BadRequest("Missing stream in devour path")
  }
}
