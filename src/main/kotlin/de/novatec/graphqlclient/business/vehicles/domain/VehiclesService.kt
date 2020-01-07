package de.novatec.graphqlclient.business.vehicles.domain

import de.novatec.graphqlclient.configuration.ApplicationProperties
import de.novatec.graphqlclient.graphql.repository.GraphQlClient
import de.novatec.graphqlclient.queries.VehiclesQuery
import mu.KotlinLogging
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.stream.Collectors

private val LOG = KotlinLogging.logger {}

class VehiclesService(private val client: GraphQlClient, private val config: ApplicationProperties) {
    suspend fun retrieveAndTransformVehicles(): List<Vehicle> {
        LOG.debug { "Retrieve all vehicles available for the demo." }
        val retrievedElements =
            ((client.query(VehiclesQuery())?.nodes?.elements ?: Collections.emptyList<VehiclesQuery.Element>()))
                .requireNoNulls()
        LOG.debug { "Transform retrieved vehicles to domain objects." }
        return retrievedElements.stream().map { element ->
            (element.inlineFragment as? VehiclesQuery.AsVehicle)!!.fields?.let {
                Vehicle(
                    it.name.orEmpty(),
                    it.slug.orEmpty(),
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