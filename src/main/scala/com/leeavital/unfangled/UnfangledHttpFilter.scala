package com.leeavital

import com.twitter.finagle.{Service, Filter => FinagleFilter}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http._

/**
 * Created by lee on 10/4/14.
 */
class UnfangledHttpFilter extends FinagleFilter[HttpRequest, HttpResponse, UnfangledRequest, UnfangledResponse] {

  def apply(req: HttpRequest, service: Service[UnfangledRequest, UnfangledResponse]): Future[HttpResponse] = {
    val unfangledRequest: UnfangledRequest = new UnfangledRequest(req)
    service(unfangledRequest).map(_.toHttpResponse(req.getProtocolVersion))
  }
}
