package org.medaware.catalyst.exception

import org.springframework.http.HttpStatus

class CatalystException(val summary: String, override val message: String, val statusCode: HttpStatus) :  RuntimeException(message)