package com.zerobase.api.exception

import java.lang.RuntimeException

class CustomException(val customErrorCode: CustomErrorCode) : RuntimeException()