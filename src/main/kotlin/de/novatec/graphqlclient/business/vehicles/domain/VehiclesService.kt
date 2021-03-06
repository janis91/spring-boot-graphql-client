package de.novatec.graphqlclient.business.vehicles.domain

import de.novatec.graphqlclient.configuration.ApplicationProperties
import de.novatec.graphqlclient.graphql.repository.GraphQlClient
import de.novatec.graphqlclient.mesh.VehiclesQuery
import mu.KotlinLogging
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.stream.Collectors
import javax.validation.ValidationException

private val LOG = KotlinLogging.logger {}

class VehiclesService(private val client: GraphQlClient, private val config: ApplicationProperties) {
    suspend fun retrieveAndTransformVehicles(): List<Vehicle> {
        LOG.debug { "Retrieve all vehicles available for the demo." }
        val retrievedElements =
            ((client.query(VehiclesQuery())?.nodes?.elements ?: throw ValidationException("The retrieved data is null for root, 'nodes' or 'elements' attribute, which is not allowed.")))
                .requireNoNulls()
        LOG.debug { "Transform retrieved vehicles to domain objects." }
        return retrievedElements.stream().map { element ->
            element.asVehicle?.fields?.let {
                Vehicle(
                    it.name.orEmpty(),
                    (it.price ?: BigDecimal.ZERO)
                        .multiply(BigDecimal.ONE.plus(config.profitMargin.divide(BigDecimal.valueOf(100))))
                        .setScale(2, RoundingMode.CEILING)
                )
            }
        }
            .collect(Collectors.toList())
            .filterNotNull()
    }
}