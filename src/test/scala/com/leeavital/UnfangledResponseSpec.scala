package com.leeavital

import org.jboss.netty.handler.codec.http.{HttpResponseStatus, HttpVersion}


/**
 * Created by lee on 10/13/14.
 */
class UnfangledResponseSpec extends UnfangledSpec {

  val version = HttpVersion.HTTP_1_1

  "UnfangledResponse" should "set content-type headers correctly" in {
    val resp = UnfangledResponse.html(HtmlString("HELLO WORLD"))
    resp.toHttpResponse(version).headers.get("Content-Type") should be("text/html")
  }

  it should "set status codes correctly" in {
    val resp = UnfangledResponse.html(HtmlString("HELLO WORLD"), HttpResponseStatus.NOT_FOUND)
    resp.toHttpResponse(version).getStatus.getCode should be(404)
    resp.toHttpResponse(version).getStatus should be(HttpResponseStatus.NOT_FOUND)
  }

  it should "set cookies correctly" in {
    val resp = UnfangledResponse.html(HtmlString("Something"), HttpResponseStatus.OK)
    resp.cookie("cook", "ie")

    resp.toHttpResponse(version).headers.getAll("Set-Cookie").toArray().toSet should be(Set("cook=ie; Secure"))
  }

  it should "not have an empty Set-Cookie header" in {

    val resp = UnfangledResponse.html(HtmlString("Something"), HttpResponseStatus.OK)
    resp.toHttpResponse(version).headers.getAll("Set-Cookie").toArray() should be(Array())
  }
}
