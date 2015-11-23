package com.leeavital

import java.net.URL
import java.io.File
import java.nio.file.{Path, Paths}
import scala.io.Source
import com.twitter.util.{Try, Future}
import javax.activation.MimetypesFileTypeMap
import com.leeavital.util.ChannelBufferHelper
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import scala.collection.mutable.{Map => MutableMap}

/**
 * Created by lee on 10/12/14.
 *
 * This is a utility to serve static files. Apply the object to a search path
 * to create a partial function that will return a served file when a file is found
 * in the search path
 *
 * val pf : PartialFunction[UnfangledRequest,UnfangledResponse] = StaticServer("webapp")
 * Unfangled.serve(pf, 5000)
 *
 * Now visiting http://localhost:5000 will lookup webapp/index.html in the class path an serve it
 *
 * TODO fix variance so this can be used from a filter
 */
object StaticServer {

  val typeMap = new MimetypesFileTypeMap()

  //TODO optional caching
  def apply(root: String) = {
    val pf: PartialFunction[UnfangledRequest, Future[UnfangledResponse]] = {
      case req => 
        val maybeResponse = for {
          uri <- Try(this.getClass.getClassLoader.getResource(root + req.uri))
          string <- Try(Source.fromURL(uri).mkString)
          contentType = typeMap.getContentType(uri.getFile)
          response = new UnfangledResponse(ChannelBufferHelper.create(string), HttpResponseStatus.OK, MutableMap("Content-Type" -> contentType))
        } yield response

        // HACK: MatchError is not a good way to go to the next partial function
        maybeResponse.getOrElse(throw new MatchError(root))
    }
    pf
  }
}
