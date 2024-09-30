package org.medaware.catalyst.dto

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param brief 
 * @param message 
 * @param status 
 */
data class CatalystError(

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("brief", required = true) val brief: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("message", required = true) val message: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: kotlin.Int
) {

}
