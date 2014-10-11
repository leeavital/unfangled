package com.leeavital

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._

@RunWith(classOf[JUnitRunner])
class HtmlStringSpec extends FlatSpec with Matchers{

  "HtmlString" should "wrap a string correctly" in {

    val htmlString = HtmlString("ABC")
    htmlString.s should be("ABC")

  }
}