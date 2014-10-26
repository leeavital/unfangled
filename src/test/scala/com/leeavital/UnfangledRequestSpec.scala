package com.leeavital

import org.scalatest._
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpVersion, DefaultHttpRequest}
import com.leeavital.util.ChannelBufferHelper

/**
 * Created by lee on 10/11/14.
 */
class UnfangledRequestSpec extends FlatSpec with Matchers {

  "UnfangledRequest" should "wrap an HTTP request correctly" in {

    val rawReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PATCH, "/foo/bar/baz")
    rawReq.setHeader("Accept", "application/json")
    rawReq.setHeader("User-Agent", "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/21.0")
    rawReq.setHeader("Cookie", "auth-token=7039; mycookie=4")
    rawReq.setContent(ChannelBufferHelper.create("FOOBAR"))



    val unfangledReq = new UnfangledRequest(rawReq)

    unfangledReq.method should be(HttpMethod.PATCH)
    unfangledReq.uri should be("/foo/bar/baz")
    unfangledReq.header("Accept") should be(Some("application/json"))
    unfangledReq.header("Timeout") should be(None)
    unfangledReq.body should be("FOOBAR")
    unfangledReq.bytes.toList should be("FOOBAR".getBytes.toList)

    unfangledReq.cookie("auth-token") match {
      case Some(c) =>
        c.getName should be("auth-token")
        c.getValue should be("7039")
      case None =>
        fail("Did not parse cookie correctly")
    }


  }

  it should "parse post params correctly" in {

    val rawReq = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PATCH, "/foo/bar/baz")
    rawReq.setContent(ChannelBufferHelper.create("a=b&r=4"))
    val ur = new UnfangledRequest(rawReq)

    val p = Params(ur)
    p.apply("a") should be(Some("b"))
    p.apply("r") should be(Some("4"))
    p.apply("notakey") should be(None)



  }
}
