package de.novatec.graphqlclient.business.graphql.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import de.novatec.graphqlclient.business.graphql.exception.GraphQlClientException
import mu.KotlinLogging

private val LOG = KotlinLogging.logger {}
class GraphQlClientImpl(private val apolloClient: ApolloClient): GraphQlClient {
    override suspend fun <D : Operation.Data, T, V : Operation.Variables> query(query: Query<D, T, V>): T {
        try {
            return apolloClient.query(query).toDeferred().await().data()!!
        } catch (e: ApolloException) {
            LOG.error (e) { "Error in GraphQl query call." }
            throw GraphQlClientException()
        }
    }
}