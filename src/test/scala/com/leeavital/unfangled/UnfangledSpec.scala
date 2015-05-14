package com.leeavital

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._

import com.leeavital.util.ChannelBufferHelper

import org.jboss.netty.handler.codec.http.{HttpMethod, HttpVersion, DefaultHttpRequest}

@RunWith(classOf[JUnitRunner])
class UnfangledSpec extends FlatSpec with Matchers {
  
  def fakeRequest(  path: String = "/",
                    content: String  = "",
                    method: HttpMethod = HttpMethod.GET ) = {
    val rawReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1, method, path)
    rawReq.headers.add("User-Agent", "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/21.0")
    rawReq.setContent(ChannelBufferHelper.create(content))

    new UnfangledRequest(rawReq)
  }
}
