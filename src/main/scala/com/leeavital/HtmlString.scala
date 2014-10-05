package com.leeavital


/**
 * valueclass wrapper for string. Anything that is an "HtmlString" is already safe to be rendered as HTML
 * @param self
 */
case class HtmlString(self: String) extends AnyVal {
  def s = self
}
