package com.leeavital.requests

import com.leeavital.UnfangledRequest
import org.jboss.netty.handler.codec.http.HttpMethod

/**
 * Created by lee on 10/5/14.
 */
object Post {
  def unapply(req: UnfangledRequest) = {
    if (req.method == HttpMethod.POST) {
      Some(req.uri)
    } else {
      None
    }
  }
}


object Get {
  def unapply(req: UnfangledRequest) = {
    if (req.method == HttpMethod.GET) {
      Some(req.uri)
    } else {
      None
    }
  }
}

object Seg {
  def unapply(uri: String): Option[List[String]] = {
    if (uri.startsWith("/")) {
      Some(uri.tail.split("/").toList)
    } else {
      Some(uri.split("/").toList)
    }
  }
}
