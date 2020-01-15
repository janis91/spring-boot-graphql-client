package de.novatec.graphqlclient.business.vehicles.domain

import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.api.ResponseFieldMarshaller
import de.novatec.graphqlclient.configuration.ApplicationProperties
import de.novatec.graphqlclient.graphql.repository.GraphQlClient
import de.novatec.graphqlclient.queries.VehiclesQuery
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import javax.validation.ValidationException

internal class VehiclesServiceTests {

    private val querySlot: CapturingSlot<VehiclesQuery> = slot()
    private val client: GraphQlClient = mockk()
    private val applicationProperties: ApplicationProperties = mockk()
    private val service: VehiclesService = VehiclesService(client, applicationProperties)

    @Test
    fun `GIVEN graphql client returns two vehicles in the list, WHEN retrieveAndTransformVehicles is called, THEN returns the corresponding list of two vehicle domain objects`() {
        val data = VehiclesQuery.Data(
            VehiclesQuery.Node(
                "nodes",
                listOf(
                    VehiclesQuery.Element(
                        "element",
                        VehiclesQuery.AsVehicle(
                            "vehicle",
                            VehiclesQuery.Field("fields", "Foo", BigDecimal.valueOf(123.12))
                        )
                    ),
                    VehiclesQuery.Element(
                        "element",
                        VehiclesQuery.AsVehicle(
                            "vehicle",
                            VehiclesQuery.Field("fields", "Bar", BigDecimal.ZERO)
                        )
                    )
                )
            )
        )
        coEvery { client.query(any<VehiclesQuery>()) } returns data
        every { applicationProperties.profitMargin } returns BigDecimal.TEN

        val result = runBlocking { service.retrieveAndTransformVehicles() }

        assertThat(result).containsExactlyInAnyOrder(
            Vehicle("Foo", BigDecimal.valueOf(135.44)),
            Vehicle("Bar", BigDecimal.ZERO.setScale(2))
        )
        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    @Test
    fun `GIVEN graphql client returns one vehicle in the list, WHEN retrieveAndTransformVehicles is called, THEN returns the corresponding list with single vehicle domain object`() {
        val data = VehiclesQuery.Data(
            VehiclesQuery.Node(
                "nodes",
                listOf(
                    VehiclesQuery.Element(
                        "element",
                        VehiclesQuery.AsVehicle(
                            "vehicle",
                            VehiclesQuery.Field("fields", "Foo", BigDecimal.valueOf(123.12))
                        )
                    )
                )
            )
        )
        coEvery { client.query(any<VehiclesQuery>()) } returns data
        every { applicationProperties.profitMargin } returns BigDecimal.TEN

        val result = runBlocking { service.retrieveAndTransformVehicles() }

        assertThat(result).containsExactlyInAnyOrder(
            Vehicle("Foo", BigDecimal.valueOf(135.44))
        )
        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    @Test
    fun `GIVEN graphql client returns an empty list, WHEN retrieveAndTransformVehicles is called, THEN returns an empty list`() {
        val data = VehiclesQuery.Data(
            VehiclesQuery.Node(
                "nodes",
                emptyList()
            )
        )
        coEvery { client.query(any<VehiclesQuery>()) } returns data

        val result = runBlocking { service.retrieveAndTransformVehicles() }

        assertThat(result).isEmpty()
        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    @Test
    fun `GIVEN graphql client returns null for elements, WHEN retrieveAndTransformVehicles is called, THEN throws ValidationException`() {
        val data = VehiclesQuery.Data(
            VehiclesQuery.Node(
                "nodes",
                null
            )
        )
        coEvery { client.query(any<VehiclesQuery>()) } returns data

        assertThatExceptionOfType(ValidationException::class.java)
            .isThrownBy { runBlocking { service.retrieveAndTransformVehicles() } }
            .withMessage("The retrieved data is null for root, 'nodes' or 'elements' attribute, which is not allowed.")

        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    @Test
    fun `GIVEN graphql client returns null for nodes, WHEN retrieveAndTransformVehicles is called, THEN throws ValidationException`() {
        val data = VehiclesQuery.Data(
            null
        )
        coEvery { client.query(any<VehiclesQuery>()) } returns data

        assertThatExceptionOfType(ValidationException::class.java)
            .isThrownBy { runBlocking { service.retrieveAndTransformVehicles() } }
            .withMessage("The retrieved data is null for root, 'nodes' or 'elements' attribute, which is not allowed.")

        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    @Test
    fun `GIVEN graphql client returns null, WHEN retrieveAndTransformVehicles is called, THEN throws ValidationException`() {
        coEvery { client.query(any<VehiclesQuery>()) } returns null

        assertThatExceptionOfType(ValidationException::class.java)
            .isThrownBy { runBlocking { service.retrieveAndTransformVehicles() } }
            .withMessage("The retrieved data is null for root, 'nodes' or 'elements' attribute, which is not allowed.")

        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    @Test
    fun `GIVEN graphql client returns null as vehicle in the list, WHEN retrieveAndTransformVehicles is called, THEN throws IllegalArgumentException`() {
        val data = VehiclesQuery.Data(
            VehiclesQuery.Node(
                "nodes",
                listOf(
                    null
                )
            )
        )
        coEvery { client.query(any<VehiclesQuery>()) } returns data

        assertThatIllegalArgumentException()
            .isThrownBy { runBlocking { service.retrieveAndTransformVehicles() } }

        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    @Test
    fun `GIVEN graphql client returns wrong type as inlineFragment in the list, WHEN retrieveAndTransformVehicles is called, THEN throws NullPointerException`() {
        val data = VehiclesQuery.Data(
            VehiclesQuery.Node(
                "nodes",
                listOf(
                    VehiclesQuery.Element(
                        "element",
                        TestFragement()
                    )
                )
            )
        )
        coEvery { client.query(any<VehiclesQuery>()) } returns data

        assertThatNullPointerException()
            .isThrownBy { runBlocking { service.retrieveAndTransformVehicles() } }

        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    @Test
    fun `GIVEN graphql client returns null as inlineFragment in the list, WHEN retrieveAndTransformVehicles is called, THEN throws NullPointerException`() {
        val data = VehiclesQuery.Data(
            VehiclesQuery.Node(
                "nodes",
                listOf(
                    VehiclesQuery.Element(
                        "element",
                        null
                    )
                )
            )
        )
        coEvery { client.query(any<VehiclesQuery>()) } returns data

        assertThatNullPointerException()
            .isThrownBy { runBlocking { service.retrieveAndTransformVehicles() } }

        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    @Test
    fun `GIVEN graphql client returns null for fields, WHEN retrieveAndTransformVehicles is called, THEN returns an empty list`() {
        val data = VehiclesQuery.Data(
            VehiclesQuery.Node(
                "nodes",
                listOf(
                    VehiclesQuery.Element(
                        "element",
                        VehiclesQuery.AsVehicle(
                            "vehicle",
                            null
                        )
                    )
                )
            )
        )
        coEvery { client.query(any<VehiclesQuery>()) } returns data

        val result = runBlocking { service.retrieveAndTransformVehicles() }

        assertThat(result).isEmpty()
        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    @Test
    fun `GIVEN graphql client returns vehicle with null values, WHEN retrieveAndTransformVehicles is called, THEN returns list with single vehicle and null safety`() {
        val data = VehiclesQuery.Data(
            VehiclesQuery.Node(
                "nodes",
                listOf(
                    VehiclesQuery.Element(
                        "element",
                        VehiclesQuery.AsVehicle(
                            "vehicle",
                            VehiclesQuery.Field("field", null, null)
                        )
                    )
                )
            )
        )
        coEvery { client.query(any<VehiclesQuery>()) } returns data
        every { applicationProperties.profitMargin } returns BigDecimal.TEN

        val result = runBlocking { service.retrieveAndTransformVehicles() }

        assertThat(result).containsOnly(Vehicle("", BigDecimal.ZERO.setScale(2)))
        coVerifyAll { client.query(capture(querySlot)) }
        assertThat(querySlot.captured).isInstanceOf(VehiclesQuery::class.java)
    }

    internal class TestFragement : VehiclesQuery.ElementNode {
        override fun marshaller(): ResponseFieldMarshaller = ResponseFieldMarshaller {
            it.writeString(ResponseField.forString("__typename", "__typename", null, false, null), "noVehicle")
        }
    }
}