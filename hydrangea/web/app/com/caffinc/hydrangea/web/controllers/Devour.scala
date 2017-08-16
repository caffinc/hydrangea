package com.caffinc.hydrangea.web.controllers

import com.caffinc.hydrangea.web.services.KafkaStreamer._
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{InjectedController, Result}

import scala.util.{Failure, Success}

/**
  * Controller to accept input from clients
  *
  * @author Sriram
  */
class Devour extends InjectedController with LazyLogging {
  /**
    * Devours messages sent via GET or POST
    *
    * @param stream Stream to send data to
    * @return Result of the operation
    */
  def devour(implicit stream: String) = Action { request =>
    gracefulStreamToKafka {
      request.body.asJson match {
        case None =>
          Json.obj("_headers" -> request.headers.toMap, "_query" -> request.queryString)
        case Some(data) =>
          Json.obj("_headers" -> request.headers.toMap, "_query" -> request.queryString, "_body" -> data)
      }
    }
  }

  /**
    * Catch-all endpoint for /devour when stream is not specified in the URL
    *
    * @return BadRequest
    */
  def devourDefault() = Action {
    BadRequest(Json.obj("error" -> "Missing stream in devour path"))
  }

  /**
    * Sends data to the underlying KafkaStreamer and gracefully handles exceptions
    *
    * @param json   Data to send
    * @param stream Stream to send data to
    * @return Result of the action
    */
  def gracefulStreamToKafka(json: => JsObject)(implicit stream: String): Result = {
    streamToKafka(stream)(json) match {
      case Success(_) =>
        Ok(Json.obj("message" -> "Devoured"))
      case Failure(e) =>
        logger.error("Stream {} unable to devour {}", stream, json, e)
        InternalServerError(Json.obj("error" -> "Unable to devour at the moment"))
    }
  }
}
