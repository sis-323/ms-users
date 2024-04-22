package com.users.msusers.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import java.util.stream.Collectors
import java.util.stream.Stream


class KeycloakJwtTokenConverter(private val jwtGrantedAuthoritiesConverter: JwtGrantedAuthoritiesConverter,
                                private val properties: TokenConverterProperties) : Converter<Jwt,
        AbstractAuthenticationToken>

{
        override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities: Collection<GrantedAuthority> = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt)!!.stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet())
        return JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt))
    }

    private fun getPrincipalClaimName(jwt: Jwt): String {
        return properties.getPrincipalAttribute()
                .map { claim: String? -> jwt.getClaimAsString(claim) }
                .orElse(jwt.getClaimAsString(JwtClaimNames.SUB))
    }


    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {

        val resourceAccess = jwt.getClaim<Map<String, Any>>("resource_access")
        val resource = resourceAccess["pm-backend"] as Map<String, Any>?
        val roles = resource!!["roles"] as Collection<String>?
        return roles!!.stream()
                .map { role: String -> SimpleGrantedAuthority("ROLE_$role") }
                .collect(Collectors.toSet())
    }
}