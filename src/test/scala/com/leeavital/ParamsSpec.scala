package com.leeavital

import org.jboss.netty.handler.codec.http.{HttpMethod, HttpVersion, DefaultHttpRequest}
import com.leeavital.util.ChannelBufferHelper

class ParamsSpec extends UnfangledSpec {

  "Params" should "parse single parameters correctly" in {

    val rawReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PATCH, "/foo/bar/baz")
    rawReq.setContent(ChannelBufferHelper.create("a=b&r=4"))
    val unfangledRequest = new UnfangledRequest(rawReq)

    val p = new PostParamParser(unfangledRequest)
    p.get("a") should be(Some("b"))
    p.get("r") should be(Some("4"))
    p.get("notakey") should be(None)
  }

  it should "parse multiple parameters correctly" in {
    val rawReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PATCH, "/foo/bar/baz")
    rawReq.setContent(ChannelBufferHelper.create("arr[]=4&arr[]=6"))
    val unfangledRequest = new UnfangledRequest(rawReq)

    val p = new PostParamParser(unfangledRequest)

    p.getMulti("arr[]").length should be(2)
    p.getMulti("arr[]").toSet should be(Set("4", "6"))
  }

  it should "not crash upon reading unset parameters" in {
    val rawReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PATCH, "/foo/bar/baz")
    rawReq.setContent(ChannelBufferHelper.create("arr[]=4&arr[]=6"))
    val unfangledRequest = new UnfangledRequest(rawReq)

    val p = new PostParamParser(unfangledRequest)
    p.getMulti("foo").length should be(0)
  }

  it should "decode URL encoded parameters" in {
    val rawReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PATCH, "/foo/bar/baz")
    rawReq.setContent(ChannelBufferHelper.create("bar=Hello%20World"))
    val unfangledRequest = new UnfangledRequest(rawReq)

    val p = new PostParamParser(unfangledRequest)
    p.get("bar") should be( Some("Hello World"))
  }
}
