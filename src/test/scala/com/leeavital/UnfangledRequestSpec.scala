package com.leeavital

import org.scalatest._
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpVersion, DefaultHttpRequest}

/**
 * Created by lee on 10/11/14.
 */
class UnfangledRequestSpec  extends FlatSpec with Matchers{

  "UnfangledRequest" should "wrap an HTTP request correctly" in {

    val rawReq  = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PATCH, "/foo/bar/baz")

    val unfangledReq = new UnfangledRequest(rawReq)

    unfangledReq.method should be(HttpMethod.PATCH)
    unfangledReq.uri should be("/foo/bar/baz")

  }
}
