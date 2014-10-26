package com.leeavital

import org.jboss.netty.handler.codec.http.multipart.{InterfaceHttpData, HttpPostRequestDecoder, Attribute}
import scala.collection.JavaConverters._

trait StringTransform[A] {
  def apply(in: String): Option[A]
}


/**
 * Parse POST parameters
 * @param request
 */
class PostParamParser(request: UnfangledRequest) {

  val dec = new HttpPostRequestDecoder(request.req)

  implicit object stringXForm extends StringTransform[String] {
    def apply(in: String): Option[String] = Some(in)
  }


  def get(key: String) = {
    Some(dec.getBodyHttpData(key)).flatMap {
      case att: Attribute =>
        Some(att.getValue)
      case _ =>
        None
    }
  }

  def getMulti(key: String): Seq[String] = {
    val maybeDatas: Option[Seq[InterfaceHttpData]] = try {
      (dec.getBodyHttpDatas(key)) match {
        case null =>  None
        case x => Some(x.asScala.toSeq)
      }
    } catch {
      case e: Throwable => println(e)
        None
    }
    maybeDatas match {
      case Some(datas) =>
        datas.map {
          case att: Attribute =>
            Some(att.getValue)
          case _ =>
            None
        }.flatten
      case None => Seq()
    }
  }
}

