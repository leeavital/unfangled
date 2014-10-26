package com.leeavital

import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder
import org.jboss.netty.handler.codec.http.multipart.Attribute

/**
 * Created by lee on 10/26/14.
 */
case class Params(request: UnfangledRequest) {
  val dec = new HttpPostRequestDecoder(request.req)

  def apply(key: String): Option[String] = {

    Some(dec.getBodyHttpData(key)).flatMap {
      case att: Attribute =>
        Some(att.getValue)
      case _ =>
        None

    }
  }
}
