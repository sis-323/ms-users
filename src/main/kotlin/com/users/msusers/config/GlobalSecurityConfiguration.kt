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
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.invoke

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
        return http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.POST, "api/v1/users/student").permitAll()
                    .requestMatchers(HttpMethod.POST, "api/v1/assignation/tutor").hasRole("create-committee")
                    .requestMatchers("/greetings/**").hasRole("ADMIN")
                    .anyRequest().permitAll()
            }

            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt -> jwt.jwtAuthenticationConverter(keycloakJwtTokenConverter) }
            }
            .csrf{csrf -> csrf.disable()
            }
        .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .build()
    }

}
