package com.leeavital

import com.leeavital.util.ChannelBufferHelper

/**
 * Created by lee on 10/11/14.
 */
class ChannelBufferHelperSpec extends UnfangledSpec {

  "ChannelBufferHelper operations" should "be inverse for String" in {

    val s: String = "FOOBAR12"

    val encoded = ChannelBufferHelper.create(s)
    val decoded = ChannelBufferHelper.extract[String](encoded)
    decoded should be(s)
  }


  it should "be inverse for Array[Byte]" in {

    val s: Array[Byte] = "abc".getBytes

    val encoded = ChannelBufferHelper.create(s)
    val decoded = ChannelBufferHelper.extract[String](encoded)
    // we need to use .toList because array comparisons are compare-by-reference
    decoded.toList should be(s.toList)
  }
}
