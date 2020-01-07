package de.novatec.graphqlclient.api.vehicles

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
internal class VehiclesRoutesE2ETests {

    @Autowired
    private lateinit var client: WebTestClient

    @Test
    fun `GIVEN the real demo graphql endpoint at https_getmesh_io, WHEN calling GET vehicles, THEN returns corresponding json content`() {
        client.get().uri("/vehicles")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$").isArray
            .jsonPath("$[0].name").isEqualTo("Gulfstream G550")
            .jsonPath("$[1].name").isEqualTo("Maize Blue Solar Car")
            .jsonPath("$[0].price").isEqualTo(46200000)
            .jsonPath("$[1].price").isEqualTo(203500.14)
    }
}