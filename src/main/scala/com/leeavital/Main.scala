package com.leeavital

import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Throw, Return, Await}
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}
import com.twitter.util._

object Main extends App {
  val service: Service[HttpRequest, HttpResponse] = (new UnfangledHttpFilter) andThen (new MyService)
  val server = Http.serve(":8080", service)
  Await.ready(server)
}

class MyService extends Service[UnfangledRequest, UnfangledResponse] {

  implicit class Futurable[A](e : A) {
    def toFuture (implicit conv : A => Future[A]) : Future[A]  = {
      conv(e)
    }
  }




  def apply(req: UnfangledRequest): Future[UnfangledResponse] = {
    val html = Templates.out(Map("title"->"Hello world"))

    html match {
      case Return(html) => UnfangledResponse.html(html).toFuture
      case Throw(e) =>
        println(e.getMessage)
        new UnfangledResponse("NO".getBytes).toFuture
    }

  }
}