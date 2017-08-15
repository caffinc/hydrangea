package com.caffinc.hydrangea.web.controllers

import play.api.mvc.{Action, InjectedController}

class Devour extends InjectedController {
  def devour() = Action { request =>
    val data = request.body.asJson
    Ok(s"Devoured $data")
  }
}
