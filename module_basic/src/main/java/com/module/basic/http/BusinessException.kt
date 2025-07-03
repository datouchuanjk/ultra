package com.module.basic.http

class BusinessException(val code: Int, message: String?) : Exception(message)