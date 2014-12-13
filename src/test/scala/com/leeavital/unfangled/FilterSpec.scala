package com.leeavital

import org.scalatest.{FlatSpec, Matchers}
import com.twitter.util.{Await, Future}

/**
 * Created by lee on 10/12/14.
 */
class FilterSpec extends FlatSpec with Matchers {

  val toStringService: PartialFunction[Double, Future[String]] = {
    case d => Future.value(d.toString)
  }

  val intIdentityService: PartialFunction[Int, Future[Int]] = {
    case e => Future.value(e)
  }

  // this turns a Double => String into an Int => Float
  object IntDoubler extends Filter[Int, String, Double, Float] {
    def apply(req: Int, service: PartialFunction[Double, Future[String]]): Future[Float] = {
      val d = req.toDouble
      service(d).map(x => x.toDouble.toFloat * 2)
    }
  }

  object SimpleIntQuadrupler extends SimpleFilter[Int, Int] {
    def apply(req: Int, service: PartialFunction[Int, Future[Int]]): Future[Int] = {
      service(req * 2).map(_ * 2)
    }
  }

  "Filter" should "transform things correctly" in {
    val service: PartialFunction[Int, Future[Float]] = IntDoubler ~> toStringService
    val f: Future[Float] = service(2)
    Await.result(f) should be(4.0)
  }

  "SimpleFilter" should "transform things correctly" in {
    val service = SimpleIntQuadrupler ~> intIdentityService
    val f = service(2)
    Await.result(f) should be(8)
  }

}
