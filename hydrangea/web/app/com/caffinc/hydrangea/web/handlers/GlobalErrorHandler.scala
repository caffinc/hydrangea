package com.caffinc.hydrangea.web.handlers

import javax.inject.Singleton

import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class GlobalErrorHandler extends HttpErrorHandler {
  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful(
      statusCode match {
        case 404 =>
          NotFound(Json.obj("error" -> "I have no memory of this place. You should probably not be here. Nothing to see here."))
        case _ =>
          Status(statusCode)(Json.obj("error" -> s"It's not us, it's you (probably). What we know: $message"))
      }
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      InternalServerError(Json.obj("error" -> s"We goofed up. What we know: ${exception.getMessage}"))
    )
  }
}
