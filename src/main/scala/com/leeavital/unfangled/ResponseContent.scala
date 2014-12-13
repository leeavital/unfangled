package com.leeavital

/**
 * Anything that is a content string can be turned into a response
 */
trait ResponseContent extends Any {
  def toResponse: UnfangledResponse
}
