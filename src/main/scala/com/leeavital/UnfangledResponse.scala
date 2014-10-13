package com.leeavital

import org.jboss.netty.handler.codec.http.{HttpVersion, DefaultHttpResponse, HttpResponse, HttpResponseStatus}
import collection.mutable.Map
import org.jboss.netty.buffer.ChannelBuffer
import com.twitter.util.Future
import com.leeavital.util.ChannelBufferHelper

/**
 * Created by lee on 10/4/14.
 */
class UnfangledResponse(val content: ChannelBuffer, val status: HttpResponseStatus = HttpResponseStatus.OK, headers: Map[String, String] = Map()) {

  def header(k: String, v: String) = {
    headers.put(k, v)
  }

  def toHttpResponse: HttpResponse = {
    val r: HttpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status)
    val channelBufferedContent = content
    r.setContent(channelBufferedContent)
    headers.foreach {
      case (k, v) =>
        r.setHeader(k, v)
    }
    r
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
