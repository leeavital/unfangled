package com.leeavital

import com.twitter.util.Future
import java.io.File
import org.fusesource.scalate.util.ClassLoaders
import java.net.URL
import scala.io.{BufferedSource, Source}

/**
 * Created by lee on 10/12/14.
 */
object StaticServer {

  def serve(rootPath: String) = {
    val matcher = FileSearcher(rootPath)
    val pf: PartialFunction[UnfangledRequest, Future[UnfangledResponse]] = {
      case  matcher(f)=>
        //TODO figure out MIME types
        UnfangledResponse.html(HtmlString(f.mkString)).toFuture
    }
    pf
  }


  //TODO optional caching
  private case class FileSearcher(root: String) {
    def unapply(req: UnfangledRequest): Option[BufferedSource] = {
        //TODO remove dependency on scalate
      for {
        fileURL : URL <- ClassLoaders.findResource(root + req.uri)
      } yield Source.fromURL(fileURL)
    }
  }
}
