package de.novatec.graphqlclient.api.vehicles

import de.novatec.graphqlclient.business.vehicles.VehiclesHandler
import org.springframework.web.reactive.function.server.coRouter

class VehiclesRoutes(private val handler: VehiclesHandler) {
    fun routes() = coRouter {
        "/vehicles".nest {
            GET("/", handler::readAll)
        }
    }
}