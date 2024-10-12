package org.medaware.catalyst.security

import org.medaware.catalyst.persistence.model.SessionEntity
import org.medaware.catalyst.security.authentication.CatalystAuthentication
import org.springframework.security.core.context.SecurityContextHolder

fun currentSession(): SessionEntity =
    (SecurityContextHolder.getContext().authentication as CatalystAuthentication).sessionEntity

fun isAuthenticated(): Boolean = SecurityContextHolder.getContext().authentication is CatalystAuthentication