package com.leeavital

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.jboss.netty.handler.codec.http.{HttpVersion, HttpResponseStatus, DefaultHttpResponse}
import java.nio.ByteBuffer
import org.jboss.netty.buffer.ByteBufferBackedChannelBuffer


/**
 * Created by lee on 10/4/14.
 */
@RunWith(classOf[JUnitRunner])
class HttpResponseWithBytesTest extends FlatSpec with Matchers {

  "HttpResponse" should "Work with byte buffers" in {
    val response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
    val bb = ByteBuffer.wrap("String".getBytes)
    response.setContent(new ByteBufferBackedChannelBuffer(bb))
  }
}