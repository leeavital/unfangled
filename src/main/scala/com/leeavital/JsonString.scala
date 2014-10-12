package com.leeavital

/**
 * valueclass wrapper for string. Anything that is a "JsonString" is already safe to be rendered as JSON
 * @param self
 */
case class JsonString(self: String) extends AnyVal {
  def s = self
}
