package com.leeavital

import org.jboss.netty.handler.codec.http.{Cookie, CookieDecoder, HttpRequest}
import com.leeavital.util.ChannelBufferHelper
import scala.collection.JavaConversions._

/**
 * Created by lee on 10/4/14.
 *
 * This class is a thin wrapper around Netty's HttpRequests
 */
class UnfangledRequest(val req: HttpRequest) {

  def method = req.getMethod

  def uri = req.getUri

  def header(key: String): Option[String] = {
    req.headers.get(key) match {
      case null => None
      case x => Some(x)
    }
  }

  def cookie(key: String): Option[String] = {
    cookies.get(key).map(_.getValue)
  }

  lazy val body = ChannelBufferHelper.extract[String](req.getContent)
  lazy val bytes = ChannelBufferHelper.extract[Array[Byte]](req.getContent)

  lazy val cookies: Map[String, Cookie] = {
    val rawCookies = req.headers.get("Cookie")
    rawCookies match {
      case null => Map.empty
      case cookieStr =>
        val cookies: Set[Cookie] = (new CookieDecoder().decode(cookieStr)).toSet
        cookies.toList.map {
          cookie: Cookie =>
            (cookie.getName, cookie)
        }.toMap
    }
  }
}
