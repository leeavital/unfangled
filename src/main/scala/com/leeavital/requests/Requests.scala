package com.leeavital.requests

import com.leeavital.UnfangledRequest
import org.jboss.netty.handler.codec.http.HttpMethod

/**
 * Created by lee on 10/5/14.
 *
 * These objects are used to pattern match against UnfangledRequest
 */
object POST {
  def unapply(req: UnfangledRequest) = {
    if (req.method == HttpMethod.POST) {
      Some(req.uri)
    } else {
      None
    }
  }
}


object GET {
  def unapply(req: UnfangledRequest) = {
    if (req.method == HttpMethod.GET) {
      Some(req.uri)
    } else {
      None
    }
  }
}

object Path {
  def unapply(uri: String): Option[List[String]] = {
    if (uri.startsWith("/")) {
      Some(uri.tail.split("/").toList)
    } else {
      Some(uri.split("/").toList)
    }
  }
}
