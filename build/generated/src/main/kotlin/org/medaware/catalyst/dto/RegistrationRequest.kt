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
 * @param userName 
 * @param firstName 
 * @param lastName 
 * @param password 
 */
data class RegistrationRequest(

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("userName", required = true) val userName: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("firstName", required = true) val firstName: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("lastName", required = true) val lastName: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("password", required = true) val password: kotlin.String
) {

}

