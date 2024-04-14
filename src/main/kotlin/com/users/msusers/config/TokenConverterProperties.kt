package com.users.msusers.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.*


@Configuration
@ConfigurationProperties(prefix = "token.converter")
class TokenConverterProperties {
    var resourceId: String? = null
    private var principalAttribute: String? = null

    fun getPrincipalAttribute(): Optional<String> {
        return Optional.ofNullable(principalAttribute)
    }

    fun setPrincipalAttribute(principalAttribute: String?) {
        this.principalAttribute = principalAttribute
    }
}