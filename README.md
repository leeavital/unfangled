[![Build Status](https://travis-ci.org/leeavital/unfangled.svg?branch=master)](https://travis-ci.org/leeavital/unfangled)


Unfangled
========

An attempt to wrap the unfiltered API on top of Finagle



Documentation
=============

__this is more of a tutorial, you can generate the complete scaladoc with `gradle scaladoc`__


Getting Started
---------------

Every unfangled server is a partial function from request to a future of a
response. Here is a simple unfangled application:

````scala
import com.leeavital._
import com.twitter.util.Future

object Main extends App {

  val server = PartialFunction[UnfangledRequest,Future[UnfangledResponse]] {
    case _ =>  UnfangledResponse.html( HtmlString("Hello world") ).toFuture
  }

  Unfangled.serve(server, port = 5000 )
}
````

This will blindly return an HTML string that says "Hello World" to the user.
First we define the `PartialFunction`:  `server`. For any input, it returns
`HtmlString("Hello World")`. `HtmlString` is a value class that promises that the
given text is safe to be rendered on a web page.  `UnfangledResponse.html` will
handle creating the response. We need to call `toFuture` because that's what the
server is expecting.


`Unfangled.serve` will take the function, and serve it forever. It should be noted,
that `Unfangled.serve` will block.


Routing
-------

Unfangled handles routing with scala's powerful pattern matching features. To make our
"Hello World" server more feature full. We could write:

````scala
import com.leeavital._
import com.leeavital.requests._
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpResponseStatus

object Main extends App {

  val rxp = """([a-zA-Z]+)""".r
  val server = PartialFunction[UnfangledRequest, Future[UnfangledResponse]] {
    case GET(Path("hello" :: rxp(name) :: Nil)) =>
      val hello = HtmlString("Hello " + name)
      UnfangledResponse.html(hello).toFuture

    case _ =>
      UnfangledResponse.html(HtmlString("invalid request"), HttpResponseStatus.BAD_REQUEST).toFuture
  }

  Unfangled.serve(server, port = 5000)
}
````

This example shows that we can filter by HTTP Method, and then by the "Path"
of the request, and then do regular expression testing on specific arguments.
For example, `GET /hello/Lee`, will work just fine. `POST /hello/lee`
will return an invalid request response.
`GET /hello/<script>do something nasty</script>` will also return an invalid
request response.

For more information on how this works, read about pattern matching
and unapplying in scala.

Objects for `GET`, `POST`, `PUT`, and `DELETE` are all defined in the package
`com.leeavital.requests`. As an exercise,
try writing an object that matches all requests!
