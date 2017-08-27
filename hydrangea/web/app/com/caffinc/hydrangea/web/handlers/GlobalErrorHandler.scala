package com.caffinc.hydrangea.web.handlers

import javax.inject.Singleton

import com.caffinc.hydrangea.web.helpers.Messaging._
import play.api.http.HttpErrorHandler
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.Future

/**
  * Handles all HTTP Errors in the application and returns a JSON error response
  *
  * @author Sriram
  */
@Singleton
class GlobalErrorHandler extends HttpErrorHandler {
  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(
      statusCode match {
        case 404 =>
          NotFound(getError("I have no memory of this place. You should probably not be here. Nothing to see here."))
        case _ =>
          Status(statusCode)(getError(s"It's not us, it's you (probably). What we know: $message"))
      }
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future.successful(
      InternalServerError(getError(s"We goofed up. What we know: ${exception.getMessage}"))
    )
  }
}
