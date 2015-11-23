package com.leeavital

import com.twitter.util._
import com.twitter.finagle.{Http, Service}
import org.jboss.netty.handler.codec.http.{HttpResponseStatus, HttpResponse, HttpRequest}
import com.twitter.util.Return

/**
 * Created by lee on 10/11/14.
 *
 * Thin wrapper for Finatra. This lets the server be simply a partial function from
 * Request => Future[response] much like the unfiltered  Request => Response
 */
object Unfangled {

  type Server = PartialFunction[UnfangledRequest, Future[UnfangledResponse]]

  lazy val internalServerError = UnfangledResponse.html(HtmlString("Internal Server Error"), HttpResponseStatus.INTERNAL_SERVER_ERROR)
  lazy val notFoundError = UnfangledResponse.html(HtmlString("Not found"), HttpResponseStatus.NOT_FOUND)

  def serve(serve: PartialFunction[UnfangledRequest, Future[UnfangledResponse]], port: Int = 8080) = {
    // create a new finagled service
    val service: Service[HttpRequest, HttpResponse] = (new UnfangledHttpFilter) andThen new Service[UnfangledRequest, UnfangledResponse] {
      def apply(request: UnfangledRequest): Future[UnfangledResponse] = Try(serve(request)) match {
        case Return(resp) => resp
        case Throw(e: MatchError) =>
          Future.value(notFoundError)
        case Throw(e) =>
          println("Failed " + e.getMessage)
          e.printStackTrace()
          Future.value(internalServerError)
      }
    }


    val httpServer = Http.serve(":" + port, service)
    Await.ready(httpServer)
  }
}
