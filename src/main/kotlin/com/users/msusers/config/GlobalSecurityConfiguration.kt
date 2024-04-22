package com.users.msusers.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import com.users.msusers.config.KeycloakJwtTokenConverter

@Configuration
@EnableWebSecurity
class GlobalSecurityConfiguration(private val properties: TokenConverterProperties) {

    private val keycloakJwtTokenConverter: KeycloakJwtTokenConverter

    init {
        val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
        this.keycloakJwtTokenConverter = KeycloakJwtTokenConverter(
            jwtGrantedAuthoritiesConverter,
            properties
        )
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeRequests { authorizeRequests ->
            authorizeRequests
                //.requestMatchers("/api/v1/users/professor").permitAll()
                .requestMatchers("api/v1/users/students").hasRole("view-students-list")
                .requestMatchers("api/v1/users/relator").hasRole("create-committee")
                .requestMatchers("api/v1/users/tutor").hasRole("create-committee")
                .anyRequest().permitAll()
        }
        http.oauth2ResourceServer { oauth2 ->
            oauth2.jwt { jwt -> jwt.jwtAuthenticationConverter(keycloakJwtTokenConverter) }
        }
        //http.csrf().disable()
        http.sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        return http.build()
    }
}
