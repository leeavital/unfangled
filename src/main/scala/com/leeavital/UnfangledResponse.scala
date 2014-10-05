package com.leeavital

import org.jboss.netty.handler.codec.http.{HttpVersion, DefaultHttpResponse, HttpResponse, HttpResponseStatus}
import collection.mutable.Map
import org.jboss.netty.buffer.ByteBufferBackedChannelBuffer
import java.nio.ByteBuffer
import com.twitter.util.Future

/**
 * Created by lee on 10/4/14.
 */
class UnfangledResponse(val content: Array[Byte], val status: HttpResponseStatus = HttpResponseStatus.OK, headers: Map[String, String] = Map()) {

  def header(k: String, v: String) = {
    headers.put(k, v)
  }

  def toHttpResponse : HttpResponse = {
    val r: HttpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status)
    r.setContent(new ByteBufferBackedChannelBuffer(ByteBuffer.wrap(content)))
    r
  }

}


object UnfangledResponse {
  type Status = HttpResponseStatus

  def html(html: HtmlString, status: Status = HttpResponseStatus.OK) = {
    val bytes = html.s.getBytes
    new UnfangledResponse(bytes, status, Map("Content-Type" -> "text/html"))
  }


  implicit def toFuture(e : UnfangledResponse ) : Future[UnfangledResponse] = {
    Future.value(e)
  }
}