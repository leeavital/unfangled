package com.leeavital.example

import org.jboss.netty.handler.codec.http.HttpResponseStatus
import com.leeavital.requests.{Path, GET}
import com.leeavital._
import com.leeavital.HtmlString
import com.twitter.util.Throw
import com.twitter.util.Return

object Main extends App {
  Unfangled.serve(LoggingFilter ~> ((MyServer.pf) orElse (StaticServer("webapp"))), port = 5000)
}

object MyServer extends {

  val pf: Unfangled.Server = {
    case GET(Path("hello" :: name :: Nil)) =>
      val html = Templates.out(Map("title" -> s"Hello ${name}"))

      html match {
        // set a dummy cookie
        case Return(html) => UnfangledResponse.html(html).cookie("session", "334232").toFuture
        case Throw(e) =>
          println(e.getMessage)
          UnfangledResponse.html(HtmlString("NOPE"), HttpResponseStatus.INTERNAL_SERVER_ERROR).toFuture
      }
  }
}
