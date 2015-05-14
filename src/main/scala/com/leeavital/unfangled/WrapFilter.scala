package com.leeavital

import com.twitter.util.Future


class WrapFilter[T, Req, Resp](
  lookup: Req => Option[T],
  reject: Req => Future[Resp] = { _ : Req => Future.never }
) extends Filter[(T, Req), Resp, Req, Resp] {

  type WrappedRequest = (T, Req)

  def apply (req: Req, service: PartialFunction[WrappedRequest, Future[Resp]]) : Future[Resp] = {
    lookup(req) match {
      case Some(x) => service((x, req))
      case None => reject(req)
    }
  }
}
