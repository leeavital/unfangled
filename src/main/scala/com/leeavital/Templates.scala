package com.leeavital

import org.fusesource.scalate._
import org.fusesource.scalate.util._
import com.twitter.util.Try

object Templates {
  val engine = new TemplateEngine
  engine.allowReload = true
  engine.allowCaching = false

  engine.resourceLoader = new FileResourceLoader {
    override def resource(uri: String): Option[Resource] = {
      for {
        resourceURL <- ClassLoaders.findResource(uri)
      } yield Resource.fromURL(resourceURL)
    }
  }


  def makeTemplateFactory(templateName: String): Map[String, Any] => Try[HtmlString] = {
    data =>
      for {
        str <- Try(engine.layout(templateName, data))
      } yield HtmlString(str)
  }

  val out = makeTemplateFactory("home.ssp")
  val login = makeTemplateFactory("login.ssp")
}
