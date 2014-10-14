package com.leeavital

class HtmlStringSpec extends UnfangledSpec {

  "HtmlString" should "wrap a string correctly" in {

    val htmlString = HtmlString("ABC")
    htmlString.s should be("ABC")
  }
}