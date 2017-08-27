package com.caffinc.hydrangea.web.controllers

import javax.inject.Singleton

import com.caffinc.hydrangea.web.helpers.Messaging._
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
@Singleton
class Absorb extends InjectedController with LazyLogging {

  /**
    * TODO: Figure out import issues with cross builds and import [[com.caffinc.hydrangea.common.constants.MessageFields]] instead
    */
  object MessageFields {
    val FIELD_HEADERS = "_headers"
    val FIELD_QUERY_STRING = "_query"
    val FIELD_REQUEST_BODY = "_body"
  }

  import MessageFields._

  /**
    * Absorbs messages sent via GET or POST
    *
    * @param stream Stream to send data to
    * @return Result of the operation
    */
  def absorb(implicit stream: String) = Action { request =>
    gracefulStreamToKafka {
      request.body.asJson match {
        case None =>
          Json.obj(FIELD_HEADERS -> request.headers.toMap, FIELD_QUERY_STRING -> request.queryString)
        case Some(data) =>
          Json.obj(FIELD_HEADERS -> request.headers.toMap, FIELD_QUERY_STRING -> request.queryString, FIELD_REQUEST_BODY -> data)
      }
    }
  }

  /**
    * Catch-all endpoint for /absorb when stream is not specified in the URL
    *
    * @return BadRequest
    */
  def reject() = Action {
    BadRequest(getError("Missing stream in absorb path"))
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
        Ok(getMessage("Absorbed"))
      case Failure(e) =>
        logger.error("Stream {} unable to absorb {}", stream, json, e)
        InternalServerError(getError("We goofed up. Unable to absorb at the moment."))
    }
  }
}
