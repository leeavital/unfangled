package com.leeavital

import com.twitter.util.{Try, Future}
import java.net.URL
import scala.io.{BufferedSource, Source}

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

  def apply(rootPath: String) = {
    val matcher = FileSearcher(rootPath)
    val pf: PartialFunction[UnfangledRequest, Future[UnfangledResponse]] = {
      case matcher(f) =>
        //TODO figure out MIME types
        UnfangledResponse.html(HtmlString(f.mkString)).toFuture
    }
    pf
  }


  //TODO optional caching
  private case class FileSearcher(root: String) {
    def unapply(req: UnfangledRequest): Option[BufferedSource] = {
      for {
        fileURL: URL <- Try(this.getClass.getClassLoader.getResource(root + req.uri)).toOption
      } yield Source.fromURL(fileURL)
    }
  }

}
