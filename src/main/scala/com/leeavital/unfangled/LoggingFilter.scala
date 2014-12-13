package com.leeavital

import com.twitter.util.Future

/**
 * Created by lee on 10/12/14.
 *
 * Utility filter for logging requests
 *
 * Unfangled.serve( LoggingFilter ~> (your server here), 5000)
 */
object LoggingFilter extends Filter[UnfangledRequest, UnfangledResponse, UnfangledRequest, UnfangledResponse] {

  def apply(req: UnfangledRequest, service: PartialFunction[UnfangledRequest, Future[UnfangledResponse]]): Future[UnfangledResponse] = {
    println(req.method + "    " + req.uri)
    service(req)
  }
}
