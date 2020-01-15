package de.novatec.graphqlclient.business.vehicles

import com.apollographql.apollo.exception.ApolloException
import de.novatec.graphqlclient.api.vehicles.VehiclesRoutes
import de.novatec.graphqlclient.business.vehicles.domain.Vehicle
import de.novatec.graphqlclient.business.vehicles.domain.VehiclesService
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import java.math.BigDecimal

internal class VehiclesHandlerTests {

    private val service: VehiclesService = mockk()
    private val vehiclesHandler: VehiclesHandler = VehiclesHandler(service)
    private val router: RouterFunction<ServerResponse> = VehiclesRoutes(vehiclesHandler).routes()
    private val testClient = WebTestClient.bindToRouterFunction(router).build()

    @Test
    fun `GIVEN a request AND the service returns list of two vehicles, WHEN readAll is called, THEN returns that list as json`() {
        coEvery { service.retrieveAndTransformVehicles() } returns listOf(
            Vehicle("Foo", BigDecimal.ONE),
            Vehicle("Bar", BigDecimal.ZERO)
        )

        testClient.get().uri("/vehicles")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody().json(
                """
                [{ 
                    "name": "Foo",
                    "price": 1
                },{ 
                    "name": "Bar",
                    "price": 0
                }]
                """
            )
        coVerifyAll { service.retrieveAndTransformVehicles() }
    }

    @Test
    fun `GIVEN a request AND the service returns an empty list, WHEN readAll is called, THEN returns the empty list`() {
        coEvery { service.retrieveAndTransformVehicles() } returns emptyList()

        testClient.get().uri("/vehicles")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody().json("[]")
        coVerifyAll { service.retrieveAndTransformVehicles() }
    }

    @Test
    fun `GIVEN a request AND the client returns valid list with single vehicle, WHEN readAll is called, THEN returns the corresponding list with one domain object "vehicle"`() {
        coEvery { service.retrieveAndTransformVehicles() } returns listOf(Vehicle("Foo", BigDecimal.ONE))

        testClient.get().uri("/vehicles")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody().json(
                """
                [{ 
                    "name": "Foo",
                    "price": 1
                }]
                """
            )
        coVerifyAll { service.retrieveAndTransformVehicles() }
    }

    @Test
    fun `GIVEN a request AND the client throws ApolloException, WHEN readAll is called, THEN throws it`() {
        coEvery { service.retrieveAndTransformVehicles() } throws ApolloException("Test")

        testClient.get().uri("/vehicles")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is5xxServerError
        coVerifyAll { service.retrieveAndTransformVehicles() }
    }
}