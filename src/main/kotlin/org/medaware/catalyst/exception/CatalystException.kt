package org.medaware.catalyst.exception

class CatalystException(val summary: String, override val message: String) :  RuntimeException(message)