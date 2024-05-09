package org.medaware.catalyst.security.authentication

import org.medaware.catalyst.persistence.entity.UserEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class CatalystAuthentication(
    val user: UserEntity,
    val token: String
) : Authentication {
    override fun getName(): String {
        return "N/A"
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getCredentials(): Any {
        return "N/A";
    }

    override fun getDetails(): Any {
        return "N/A"
    }

    override fun getPrincipal(): Any {
        return this;
    }

    override fun isAuthenticated(): Boolean {
        return true;
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {}
}