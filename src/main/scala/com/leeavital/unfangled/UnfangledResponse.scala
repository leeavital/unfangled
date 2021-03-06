package com.leeavital

import org.jboss.netty.handler.codec.http._
import collection.mutable.Map
import org.jboss.netty.buffer.ChannelBuffer
import com.twitter.util.Future
import com.leeavital.util.ChannelBufferHelper
import scala.collection.mutable

/**
 * Created by lee on 10/4/14.
 */
class UnfangledResponse(val content: ChannelBuffer, val status: HttpResponseStatus = HttpResponseStatus.OK, headers: Map[String, String] = Map()) {

  val cookies = mutable.Set[Cookie]()

  def header(k: String, v: String) = {
    headers.put(k, v)
  }

  def toHttpResponse(version: HttpVersion): HttpResponse = {
    val r: HttpResponse = new DefaultHttpResponse(version, status)
    val channelBufferedContent = content
    r.setContent(channelBufferedContent)
    headers.foreach {
      case (k, v) =>
        r.headers.add(k, v)
    }

    // take care of cookies
    if(!cookies.isEmpty) {
      val cookieEncoder = new CookieEncoder(false)
      cookies.foreach(cookieEncoder.addCookie)
      r.headers.add("Set-Cookie", cookieEncoder.encode + "; Path=/")
    }

    r
  }

  private def addCookie(c: Cookie) = {
    cookies.add(c)
  }

  //TODO figure out sane defaults and add optional params
  def cookie(name: String, value: String) = {
    val c = new DefaultCookie(name, value)
    addCookie(c)
    this
  }

  def toFuture = {
    Future.value(this)
  }

}


object UnfangledResponse {
  type Status = HttpResponseStatus

  def html(html: HtmlString, status: Status = HttpResponseStatus.OK) = {
    val channelBuffer = ChannelBufferHelper.create(html.s)
    new UnfangledResponse(channelBuffer, status, Map("Content-Type" -> "text/html"))
  }


  def json(json: JsonString, status: Status = HttpResponseStatus.OK) = {
    val channelBuffer = ChannelBufferHelper.create(json.s)
    new UnfangledResponse(channelBuffer, status, Map("Content-Type" -> "application/json"))
  }

  implicit def toFuture(e: UnfangledResponse): Future[UnfangledResponse] = {
    Future.value(e)
  }
}
