package com.leeavital.example

import org.jboss.netty.handler.codec.http.HttpResponseStatus
import com.leeavital.requests.GET
import com.leeavital._
import com.leeavital.HtmlString
import com.twitter.util.Throw
import com.twitter.util.Return

object Main extends App {
  Unfangled.serve(MyServer.pf, port = 5000)
}


object MyServer extends {

  val pf: Unfangled.Server = {
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