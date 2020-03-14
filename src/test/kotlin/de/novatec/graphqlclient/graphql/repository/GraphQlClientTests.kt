package de.novatec.graphqlclient.graphql.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.*
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.response.ScalarTypeAdapters
import de.novatec.graphqlclient.graphql.exception.GraphQlClientException
import io.mockk.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import okio.BufferedSource
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.apollographql.apollo.api.Error as ApolloError

internal class GraphQlClientTests {

    private val apolloClient: ApolloClient = mockk()
    private val graphQlClient: GraphQlClient =
        GraphQlClient(apolloClient)

    @BeforeEach
    fun init() {
        mockkStatic("com.apollographql.apollo.coroutines.CoroutinesExtensionsKt")
    }

    @AfterEach
    fun clear() {
        unmockkStatic("com.apollographql.apollo.coroutines.CoroutinesExtensionsKt")
        clearMocks(apolloClient)
    }

    @Test
    fun `GIVEN a succeeding test query without errors, WHEN query is called, THEN returns corresponding test data`() {
        val testQuery = TestQuery()
        val expected: TestQuery.Data = mockk()
        val response: Response<TestQuery.Data> = mockk {
            every { data() } returns expected
            every { hasErrors() } returns false
        }
        val apolloCall: ApolloQueryCall<TestQuery.Data> = mockk {
            coEvery { toDeferred() } returns CompletableDeferred(response)
        }
        every { apolloClient.query(any<Query<TestQuery.Data, TestQuery.Data, TestQuery.Variables>>()) } returns apolloCall

        val result = runBlocking { graphQlClient.query(testQuery) }

        assertThat(result).isEqualTo(expected)
        verifyAll {
            response.data()
            response.hasErrors()
            apolloClient.query(testQuery)
        }
        coVerifyAll { apolloCall.toDeferred() }
    }

    @Test
    fun `GIVEN a succeeding test query with errors, WHEN query is called, THEN throws GraphQlClientException with correct cause`() {
        val testQuery = TestQuery()
        val expected: TestQuery.Data = mockk()
        val response: Response<TestQuery.Data> = mockk {
            every { data() } returns expected
            every { hasErrors() } returns true
            every { errors() } returns listOf(ApolloError("FooBar", null, null))
        }
        val apolloCall: ApolloQueryCall<TestQuery.Data> = mockk {
            coEvery { toDeferred() } returns CompletableDeferred(response)
        }
        every { apolloClient.query(any<Query<TestQuery.Data, TestQuery.Data, TestQuery.Variables>>()) } returns apolloCall

        assertThatExceptionOfType(GraphQlClientException::class.java)
            .isThrownBy { runBlocking { graphQlClient.query(testQuery) } }
            .withCauseInstanceOf(ApolloException::class.java)
            .withMessageContaining("FooBar")

        verifyAll {
            response.hasErrors()
            response.errors()
            apolloClient.query(testQuery)
        }
        coVerifyAll { apolloCall.toDeferred() }
    }

    @Test
    fun `GIVEN a failing test query, WHEN query is called, THEN returns corresponding test data`() {
        val testQuery = TestQuery()
        val apolloCall: ApolloQueryCall<TestQuery.Data> = mockk {
            coEvery { toDeferred() } returns CompletableDeferred<Response<TestQuery.Data>>()
                .apply {
                    completeExceptionally(ApolloException("FooBar"))
                }
        }
        every { apolloClient.query(any<Query<TestQuery.Data, TestQuery.Data, TestQuery.Variables>>()) } returns apolloCall

        assertThatExceptionOfType(GraphQlClientException::class.java)
            .isThrownBy { runBlocking { graphQlClient.query(testQuery) } }
            .withCauseInstanceOf(ApolloException::class.java)
            .withMessageContaining("FooBar")

        verifyAll {
            apolloClient.query(testQuery)
        }
        coVerifyAll { apolloCall.toDeferred() }
    }

    class TestQuery : Query<TestQuery.Data, TestQuery.Data, TestQuery.Variables> {
        override fun wrapData(data: Data?): Data {
            return Data()
        }

        override fun variables(): Variables {
            return Variables()
        }

        override fun queryDocument(): String {
            return ""
        }

        override fun responseFieldMapper(): ResponseFieldMapper<Data> {
            return ResponseFieldMapper { Data() }
        }

        override fun operationId(): String {
            return ""
        }

        override fun name(): OperationName {
            return OperationName { "" }
        }

        class Variables : Operation.Variables()

        class Data : Operation.Data {
            override fun marshaller(): ResponseFieldMarshaller {
                return ResponseFieldMarshaller { mockk() }
            }
        }

        override fun parse(p0: BufferedSource, p1: ScalarTypeAdapters): Response<Data> {
            TODO("Not yet implemented")
        }

        override fun parse(p0: BufferedSource): Response<Data> {
            TODO("Not yet implemented")
        }
    }
}