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

  val helloTemplate = { name : String =>
    HtmlString(s"Hello ${name}")
  }

  val whoamiTemplate = { name : String =>
    HtmlString(s"I am ${name}")
  }

  val  notFound = HtmlString("Idk")

  val pf: Unfangled.Server = {
    case GET(Path("hello" :: name :: Nil)) =>
      val html = helloTemplate(name)
      UnfangledResponse.html(html).cookie("name", name).toFuture
    case req @ GET(Path("whoami" :: Nil)) =>
      req.cookie("name") match {
        case Some(name) => UnfangledResponse.html(whoamiTemplate(name))
        case None => UnfangledResponse.html(notFound)
      }
  }
}
