package de.novatec.graphqlclient.business.vehicles

import de.novatec.graphqlclient.business.vehicles.domain.VehiclesService
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait

private val LOG = KotlinLogging.logger {}

class VehiclesHandler(private val service: VehiclesService) {
    suspend fun readAll(request: ServerRequest): ServerResponse {
        LOG.debug { "GET all vehicles available for the demo." }
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(service.retrieveAndTransformVehicles())
    }
}
