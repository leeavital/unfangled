package com.leeavital

import com.twitter.finagle.{Service, Filter}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http._

/**
 * Created by lee on 10/4/14.
 */
class UnfangledHttpFilter extends Filter[HttpRequest, HttpResponse, UnfangledRequest, UnfangledResponse] {

  def apply(req: HttpRequest, service: Service[UnfangledRequest, UnfangledResponse]): Future[HttpResponse] = {
    val unfangledRequest: UnfangledRequest = new UnfangledRequest(req)
    service(unfangledRequest).map(_.toHttpResponse)
  }
}