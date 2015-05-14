package com.leeavital

import com.twitter.util.Future

/**
 * Created by lee on 10/12/14.
 * ReqIn is short for Request-Inner, ReqOut for outer, etc.
 */
abstract class Filter[ReqIn, RespIn, ReqOut, RespOut] {

  final def ~>(service: PartialFunction[ReqIn, Future[RespIn]]) = {
    val self = this
    PartialFunction[ReqOut, Future[RespOut]] {
      case req =>
        (self.apply(req, service): Future[RespOut])
    }
  }

  def apply(req: ReqOut, service: PartialFunction[ReqIn, Future[RespIn]]): Future[RespOut]
}

/*
 * Often, the filter does not change the types of requests/responses
 */
abstract class SimpleFilter[Req, Resp] extends Filter[Req, Resp, Req, Resp]
