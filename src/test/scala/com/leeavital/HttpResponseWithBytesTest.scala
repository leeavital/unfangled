package com.leeavital

import org.jboss.netty.handler.codec.http.{HttpVersion, HttpResponseStatus, DefaultHttpResponse}
import java.nio.ByteBuffer
import org.jboss.netty.buffer.ByteBufferBackedChannelBuffer


class HttpResponseWithBytesTest extends UnfangledSpec {

  "HttpResponse" should "Work with byte buffers" in {
    val response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
    val bb = ByteBuffer.wrap("String".getBytes)
    response.setContent(new ByteBufferBackedChannelBuffer(bb))
  }
}