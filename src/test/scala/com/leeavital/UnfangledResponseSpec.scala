package com.leeavital

import org.jboss.netty.handler.codec.http.HttpResponseStatus

/**
 * Created by lee on 10/13/14.
 */
class UnfangledResponseSpec extends UnfangledSpec {

  "UnfangledResponse" should "set content-type headers correctly" in {
    val resp = UnfangledResponse.html(HtmlString("HELLO WORLD"))
    resp.toHttpResponse.headers.get("Content-Type") should be("text/html")
  }

  it should "set status codes correctly" in {
    val resp = UnfangledResponse.html(HtmlString("HELLO WORLD"), HttpResponseStatus.NOT_FOUND)
    resp.toHttpResponse.getStatus.getCode should be(404)
    resp.toHttpResponse.getStatus should be(HttpResponseStatus.NOT_FOUND)
  }

  it should "set cookies correctly" in {
    val resp = UnfangledResponse.html(HtmlString("Something"), HttpResponseStatus.OK)
    resp.cookie("cook", "ie")

    resp.toHttpResponse.headers.getAll("Set-Cookie").toArray().toSet should be(Set("cook=ie; Secure"))
  }

  it should "not have an empty Set-Cookie header" in {

    val resp = UnfangledResponse.html(HtmlString("Something"), HttpResponseStatus.OK)
    resp.toHttpResponse.headers.getAll("Set-Cookie").toArray() should be(Array())
  }
}
