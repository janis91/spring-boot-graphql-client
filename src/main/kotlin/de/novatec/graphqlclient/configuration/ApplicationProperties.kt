package de.novatec.graphqlclient.configuration

import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.math.BigDecimal
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ConfigurationProperties("graphql-client")
@Validated
class ApplicationProperties {
    /**
     * The github server url
     */
    @URL
    lateinit var serverUrl: String
    /**
     * The profit margin in percent
     */
    @Min(1)
    @NotNull
    lateinit var profitMargin: BigDecimal
}