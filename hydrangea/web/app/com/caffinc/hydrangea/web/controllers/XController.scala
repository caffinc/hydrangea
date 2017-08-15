package com.caffinc.hydrangea.web.controllers

import play.api.mvc.InjectedController

class XController extends InjectedController {
  def hello(name: String) = Action {
    Ok(s"Hello, ${name}")
  }
}

