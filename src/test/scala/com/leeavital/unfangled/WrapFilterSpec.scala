package com.leeavital

import com.twitter.util.Future
import com.twitter.util.{Await, Future, TimeoutException, Duration}
import com.twitter.conversions.time._


class WrapFilterSpec extends UnfangledSpec {
  
  val doubleLiftService : PartialFunction[(String, Int), Future[(String,Double)]]  = {
    case (s, d) => Future.value( (s, 2.0 * d) )
  }

  val passThroughEven = { x : Int =>
    if( x %  2 == 0 ) {
      Some(x.toString)
    } else {
      None 
    }
  }


  "WrapFilter" should "return correct value when lookup is successful" in {
      
      val service = 
        new WrapFilter[String,Int,(String,Double)](passThroughEven) ~>  doubleLiftService
    

      val rst = Await.result(service(4), 1.second)

      rst should be (("4", 8.0))
  }

  "WrapFilter" should "never return when lookup is unsuccessful" in {
      val service = 
        new WrapFilter[String,Int,(String,Double)](passThroughEven) ~>  doubleLiftService

      val _ = intercept[TimeoutException] {
        val e = Await.result(service(5), 1.seconds)
      }
  }

  "WrapFilter" should "pass through to the reject handler when lookup is unsuccessful" in {
    val service =
      new WrapFilter[String,Int,(String,Double)](
        passThroughEven,
        { i => Future.value(("Error", -1.0 * i)) } ) ~> doubleLiftService


    Await.result(service(5)) should be( ("Error", -5.0))
  }
}
