package org.medaware.catalyst.error

import org.springframework.http.HttpStatus

class CatalystException(
    val brief: String,
    override val message: String,
    val code: HttpStatus
) : RuntimeException(message)