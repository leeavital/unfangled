package com.leeavital

import org.scalatest.{FlatSpec, Matchers}
import org.jboss.netty.handler.codec.http.HttpResponseStatus

/**
 * Created by lee on 10/13/14.
 */
class UnfangledResponseSpec extends FlatSpec with Matchers {

  "UnfangledResponse" should "set content-type headers correctly" in {
    val resp = UnfangledResponse.html(HtmlString("HELLO WORLD"))
    resp.toHttpResponse.getHeader("Content-Type") should be("text/html")
  }

  it should "set status codes correctly" in {
    val resp = UnfangledResponse.html(HtmlString("HELLO WORLD"), HttpResponseStatus.NOT_FOUND)
    resp.toHttpResponse.getStatus.getCode should be(404)
    resp.toHttpResponse.getStatus should be(HttpResponseStatus.NOT_FOUND)
  }
}
