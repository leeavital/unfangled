package com.leeavital

import org.jboss.netty.handler.codec.http.HttpRequest
import com.leeavital.util.ChannelBufferHelper

/**
 * Created by lee on 10/4/14.
 *
 * This class is a thin wrapper around Netty's HttpRequests
 */
class UnfangledRequest(val req: HttpRequest) {

  def method = req.getMethod

  def uri = req.getUri

  def header(key: String): Option[String] = {
    req.getHeader(key) match {
      case null => None
      case x => Some(x)
    }
  }

  lazy val body = ChannelBufferHelper.extract[String](req.getContent)
  lazy val bytes = ChannelBufferHelper.extract[Array[Byte]](req.getContent)
}
