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

