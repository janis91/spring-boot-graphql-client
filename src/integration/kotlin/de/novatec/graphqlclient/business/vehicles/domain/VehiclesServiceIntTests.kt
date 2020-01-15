package de.novatec.graphqlclient.business.vehicles.domain

import de.novatec.graphqlclient.graphql.test.CMSGraphqlWireMockStubber
import de.novatec.graphqlclient.graphql.test.CMSWireMockConfiguration
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ContextConfiguration
import java.math.BigDecimal

@AutoConfigureWireMock(port = 9090)
@SpringBootTest
@ContextConfiguration(classes = [CMSWireMockConfiguration::class])
internal class VehiclesServiceIntTests {

    @Autowired
    private lateinit var vehiclesService: VehiclesService

    @Autowired
    private lateinit var stubber: CMSGraphqlWireMockStubber

    @Test
    fun `GIVEN api call results in vehicles in list, WHEN retrieveAndTransformVehicles is called, THEN returns corresponding vehicle domain objects`() {
        stubber.stubVehicles()

        val result = runBlocking { vehiclesService.retrieveAndTransformVehicles() }

        assertThat(result).containsExactlyInAnyOrder(
            Vehicle("Gulfstream G550",  BigDecimal.valueOf(46200000).setScale(2)),
            Vehicle("Maize Blue Solar Car",  BigDecimal.valueOf(203500.14))
        )
    }
}