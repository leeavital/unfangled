package com.leeavital

import com.twitter.util.{Await, Future}
import com.twitter.finagle.{Http, Service}
import org.jboss.netty.handler.codec.http.{HttpResponse, HttpRequest}

/**
 * Created by lee on 10/11/14.
 *
 * Thin wrapper for Finatra. This lets the server be simply a partial function from
 * Request => Future[response] much like the unfiltered  Request => Response
 */
object Unfangled {

  type Server = PartialFunction[UnfangledRequest, Future[UnfangledResponse]]

  def serve( serve: PartialFunction[UnfangledRequest, Future[UnfangledResponse]], port : Int = 8080) = {
    // create a new finagled service
    val service : Service[HttpRequest, HttpResponse] = (new UnfangledHttpFilter) andThen new Service[UnfangledRequest, UnfangledResponse]  {
      def apply(request: UnfangledRequest): Future[UnfangledResponse] = serve(request)
    }

    val httpServer = Http.serve(":" + port, service )
    Await.ready(httpServer)

  }
}
