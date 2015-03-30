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

  def apply(rootPath: String) = {
    val matcher = FileSearcher(rootPath)
    val pf: PartialFunction[UnfangledRequest, Future[UnfangledResponse]] = {
      case matcher(file) =>
        val string = Source.fromFile(file).mkString
        val contentType = typeMap.getContentType(file)
        new UnfangledResponse(ChannelBufferHelper.create(string), HttpResponseStatus.OK, MutableMap("Content-Type" -> contentType))
    }
    pf
  }

  //TODO optional caching
  private case class FileSearcher(root: String) {
    val path = Paths.get(root)
    def unapply(req: UnfangledRequest): Option[File] = {
      val relPath = path.resolve(req.uri).toString
      val url = this.getClass.getClassLoader.getResource(relPath)
      Try(new File(url.getFile)).toOption
    }
  
  }

}
