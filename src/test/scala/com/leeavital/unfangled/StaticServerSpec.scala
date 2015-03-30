package com.leeavital

import org.jboss.netty.handler.codec.http.{HttpResponseStatus, HttpVersion}
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpVersion, DefaultHttpRequest}
import com.twitter.util.Await


/**
 * Created by lee on 10/13/14.
 */
class StaticServerTest extends UnfangledSpec {


  val version = HttpVersion.HTTP_1_1
  
  def request(path: String, before: DefaultHttpRequest => DefaultHttpRequest = identity) = {

    val rawReq = before(new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path))
    val unfangledReq = new UnfangledRequest(rawReq)
    unfangledReq

  }

  "StaticServer" should "match files that exist" in {

    val ss = StaticServer("test_webapp/")
    val req = request("test1.txt")
    val resp = ss.apply(req)
    // should not throw any exceptions
  }

  it should "not match files that do not exist" in {
    val ss = StaticServer("test_webapp/")
    val req = request("not_a_file.txt")

    intercept[MatchError] {
      val resp = ss.apply(req)
    }
  }

  it should "read the mime type of a text file" in {
    val ss = StaticServer("test_webapp/")
    val req = request("test1.txt")
    val resp = ss.apply(req)
    val nowResult : UnfangledResponse = Await.result(resp)
    nowResult.toHttpResponse(version).headers.get("Content-Type") should be("text/plain")
  }

  it should "match the same file multiple times" in {
    // Lot's of java.io calls here; we want to make sure there aren't any side effects
    val ss = StaticServer("test_webapp/")
    val req = request("test1.txt")
    val resp = ss.apply(req)

    ss.apply(req)
    ss.apply(req)
    ss.apply(req)
    ss.apply(req)
  }
}
