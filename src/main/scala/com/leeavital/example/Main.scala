package com.leeavital.example

import com.twitter.finagle.{Http, Service}
import org.jboss.netty.handler.codec.http.{HttpResponseStatus, HttpRequest, HttpResponse}
import com.twitter.util._
import com.leeavital.requests.GET
import com.leeavital.{HtmlString, UnfangledResponse, Templates, Unfangled}

object Main extends App {
  Unfangled.serve( MyServer.pf, port=5000 )
}

object MyServer extends  {

  val pf : Unfangled.Server  = {
    case GET(uri) =>
      val html = Templates.out(Map("title" -> uri.toUpperCase()))

      html match {
        case Return(html) => UnfangledResponse.html(html).toFuture
        case Throw(e) =>
          println(e.getMessage)
          UnfangledResponse.html(HtmlString("NOPE"), HttpResponseStatus.INTERNAL_SERVER_ERROR).toFuture
      }
  }
}