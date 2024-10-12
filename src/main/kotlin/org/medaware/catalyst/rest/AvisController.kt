package org.medaware.catalyst.rest

import org.medaware.avis.css.AvisCss
import org.medaware.catalyst.api.AVISApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AvisController : AVISApi {

    override fun retrieveAvisCss(): ResponseEntity<String> {
        return ResponseEntity.ok(AvisCss.cssString())
    }

}