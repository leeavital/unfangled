package com.leeavital

import org.jboss.netty.handler.codec.http._
import org.junit.runner.RunWith
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.junit.JUnitRunner
import com.leeavital.requests.{Path, GET, POST}

/**
 * Created by lee on 10/5/14.
 */
@RunWith(classOf[JUnitRunner])
class RequestUnapplySpec extends FlatSpec with Matchers {


  "Post" should "unapply" in {
    val respRaw: HttpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.POST, "/foo")
    val unfangledRequest = new UnfangledRequest(respRaw)

    unfangledRequest match {
      case POST(s) => s should be("/foo")
      case _ => fail("Nothing")
    }
  }

  "Get" should "unapply" in {
    val respRaw: HttpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/foo")
    val unfangledRequest = new UnfangledRequest(respRaw)

    unfangledRequest match {
      case GET(s) => s should be("/foo")
      case _ => fail("Nothing")
    }
  }

  "Seg" should "unapply" in {
    val uri: String = "foo/bar"

    val foo: Option[List[String]] = Path.unapply(uri)
    foo should be(Some(Seq("foo", "bar")))

    uri match {
      case Path("foo" :: "bar" :: Nil) =>
      case x => fail("Did not unapply")
    }
  }

  it should "unapply with an initial slash" in {
    val uri: String = "/foo/bar"

    val foo: Option[List[String]] = Path.unapply(uri)
    foo should be(Some(Seq("foo", "bar")))

    uri match {
      case Path("foo" :: "bar" :: Nil) =>
      case x => fail("Did not unapply")
    }
  }

  it should "unapply with a trailing slash" in {
    val uri: String = "foo/bar/"
    val foo = Path.unapply(uri)
    foo should be(Some(Seq("foo", "bar")))

    uri match {
      case Path("foo" :: "bar" :: Nil) =>
      case _ => fail("Did not unapply")
    }
  }

  "A full HTTP request" should "be able to match Seg inside a post" in {
    val respRaw: HttpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.POST, "/foo/bar/baz")
    val unfangledRequest = new UnfangledRequest(respRaw)

    unfangledRequest match {
      case POST(Path("foo"::"bar" :: baz :: Nil)) => baz should be("baz")
      case _ => fail("Nothing")
    }
  }
}
