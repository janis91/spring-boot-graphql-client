package de.novatec.graphqlclient.graphql.test

import de.novatec.graphqlclient.configuration.ApplicationProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class CMSWireMockConfiguration {
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    fun getGraphqlWireMockStubber(configurationProperties: ApplicationProperties): CMSGraphqlWireMockStubber {
        return CMSGraphqlWireMockStubber(
            configurationProperties.serverUrl
        )
    }
}
