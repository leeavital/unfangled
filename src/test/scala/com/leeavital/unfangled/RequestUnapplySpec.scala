package com.leeavital

import org.jboss.netty.handler.codec.http.{HttpRequest, HttpMethod, HttpVersion, DefaultHttpRequest}
import com.leeavital.requests.{PUT, DELETE, GET, POST, Path}


class RequestUnapplySpec extends UnfangledSpec {


  "POST" should "unapply" in {
    val respRaw: HttpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.POST, "/foo")
    val unfangledRequest = new UnfangledRequest(respRaw)

    unfangledRequest match {
      case POST(s) => s should be("/foo")
      case _ => fail("Nothing")
    }
  }

  "GET" should "unapply" in {
    val respRaw: HttpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/foo")
    val unfangledRequest = new UnfangledRequest(respRaw)

    unfangledRequest match {
      case GET(s) => s should be("/foo")
      case _ => fail("Nothing")
    }
  }

  "DELETE" should "unapply" in {
    val respRaw = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.DELETE, "/bar")

    val unfangledRequest = new UnfangledRequest(respRaw)

    unfangledRequest match {
      case DELETE(s) => s should be("/bar")
      case _ => fail("Nothing")
    }
  }

  "PUT" should "unapply" in {
    val respRaw = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, "/bar")

    val unfangledRequest = new UnfangledRequest(respRaw)

    unfangledRequest match {
      case PUT(s) => s should be("/bar")
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
      case POST(Path("foo" :: "bar" :: baz :: Nil)) => baz should be("baz")
      case _ => fail("Nothing")
    }
  }
}
