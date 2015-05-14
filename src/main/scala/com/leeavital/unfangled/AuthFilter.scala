package com.leeavital

class AuthFilter[T,A,B](lookup: A => Option[T]) extends WrapFilter[T,A,B](lookup)
