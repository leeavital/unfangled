package com.leeavital

import com.twitter.util.{Await, Future, TimeoutException, Duration}
import com.twitter.util.Duration
import com.twitter.conversions.time._
import com.leeavital.util.ChannelBufferHelper._


class AuthFilterSpec extends UnfangledSpec {

  val echoName : PartialFunction[(String,UnfangledRequest), Future[UnfangledResponse]] =  {
    case (e, _) => 
      UnfangledResponse.html(HtmlString(e))
  }

  "AuthFilter" should "return never return when lookup return None" in {
    

    val server = new AuthFilter[String, UnfangledRequest, UnfangledResponse]({
        _ => None
    }) ~> echoName

    val _ = intercept[TimeoutException] {
      val e = Await.result( server(fakeRequest()), 1.seconds )
    }
  }

  "AuthFilter" should "return the name when lookup returns a name" in {
    val server = new AuthFilter[String, UnfangledRequest, UnfangledResponse]({
        _ => Some("Lee")
    }) ~> echoName

    val rst = Await.result( server(fakeRequest()), 1.seconds ) 
    extract[String](rst.content) should be("Lee")
  }


  "AuthFilter" should "be able to access the adjoining request" in {
    
    val server = new AuthFilter[String, UnfangledRequest, UnfangledResponse]({
      req => Some(req.req.getUri)
    }) ~> echoName

    val resp = Await.result( server(fakeRequest(path = "/hello/world")), 1.second)

    extract[String](resp.content) should be("/hello/world")

  }
}
