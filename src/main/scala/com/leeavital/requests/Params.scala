package com.leeavital.requests

import com.leeavital.{PostParamParser, UnfangledRequest}

object Params {
  
  def unapply(req: UnfangledRequest) = {
    new PostParamParser(req)
  }

}
