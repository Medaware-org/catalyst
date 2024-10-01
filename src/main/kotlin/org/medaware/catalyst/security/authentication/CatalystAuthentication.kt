package org.medaware.catalyst.security.authentication

import org.medaware.catalyst.persistence.model.SessionEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class CatalystAuthentication(
    val sessionEntity: SessionEntity
) : Authentication {

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return listOf()
    }

    override fun getCredentials(): Any? {
        return ""
    }

    override fun getDetails(): Any? {
        return ""
    }

    override fun getPrincipal(): Any? {
        return ""
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }

    override fun getName(): String? {
        return ""
    }

}