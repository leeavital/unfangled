package com.leeavital

import org.jboss.netty.handler.codec.http.HttpRequest

/**
 * Created by lee on 10/4/14.
 */
class UnfangledRequest(val req: HttpRequest) {

  def method = req.getMethod

  def uri = req.getUri
}
