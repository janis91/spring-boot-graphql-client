package de.novatec.graphqlclient.configuration

import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@ConfigurationProperties("graphql-client")
@Validated
class ApplicationProperties {
    /**
     * The github server url
     */
    @URL
    lateinit var serverUrl: String
    /**
     * The github token
     */
    @NotBlank
    lateinit var githubAuthToken: String
}