package com.leeavital.util

import org.jboss.netty.buffer.{ChannelBuffer, ByteBufferBackedChannelBuffer}
import java.nio.ByteBuffer

/**
 * Created by lee on 10/11/14.
 */
object ChannelBufferHelper {

  // actual operations
  def create[T](s: T)(implicit converter: ByteArrayConverter[T]): ChannelBuffer = {
    val byteBuffer: ByteBuffer = ByteBuffer.wrap(converter.encode(s))
    new ByteBufferBackedChannelBuffer(byteBuffer)
  }

  def extract[T](cb: ChannelBuffer)(implicit converter: ByteArrayConverter[T]): T = {
    converter.decode(cb.array())
  }


  // adapters
  abstract class ByteArrayConverter[T] {
    def encode(in: T): Array[Byte]

    def decode(in: Array[Byte]): T
  }

  implicit val stringConverter = new ByteArrayConverter[String] {
    def encode(in: String) = {
      in.getBytes
    }


    def decode(in: Array[Byte]) = {
      new String(in)
    }
  }

  implicit val identityConverter = new ByteArrayConverter[Array[Byte]] {
    def encode(in: Array[Byte]) = in

    def decode(in: Array[Byte]) = in
  }

}
