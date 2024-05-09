package org.medaware.catalyst.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @param status
 */
data class CatalystStatus200Response(

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: kotlin.String
) {

}

