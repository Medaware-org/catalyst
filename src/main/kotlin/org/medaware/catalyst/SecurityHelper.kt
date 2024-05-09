package org.medaware.catalyst

import org.medaware.catalyst.persistence.entity.UserEntity
import org.medaware.catalyst.security.authentication.CatalystAuthentication
import org.springframework.security.core.context.SecurityContextHolder

fun authentication(): CatalystAuthentication {
    return (SecurityContextHolder.getContext().authentication) as CatalystAuthentication
}

fun authenticatedUser(): UserEntity {
    return authentication().user
}