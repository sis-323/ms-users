package com.users.msusers.config
import org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakClientConfig {
    companion object {
        private val logger = LoggerFactory.getLogger(KeycloakClientConfig::class.java.name)
    }
    @Value("\${keycloak.auth-server-url}")
    private val authUrl: String? = null

    @Value("\${keycloak.credentials.realm}")
    private val realm: String? = null

    @Value("\${keycloak.credentials.resource}")
    private val clientId: String? = null

    @Value("\${keycloak.credentials.secret}")
    private val secretKey: String? = null
    @Bean
    fun keycloak(): Keycloak {
        logger.info("Creating Keycloak client")
        logger.info("resource: $clientId")
        return KeycloakBuilder.builder()
                .grantType(CLIENT_CREDENTIALS)
                .serverUrl(authUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(secretKey)
                .build()
    }
}