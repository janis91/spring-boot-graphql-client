package de.novatec.graphqlclient.graphql.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import de.novatec.graphqlclient.graphql.exception.GraphQlClientException
import mu.KotlinLogging

private val LOG = KotlinLogging.logger {}

class GraphQlClient(private val apolloClient: ApolloClient) {
    suspend fun <D : Operation.Data, T, V : Operation.Variables> query(query: Query<D, T, V>): T? {
        try {
            LOG.debug { "Query data from graphql." }
            val response: Response<T> = apolloClient.query(query).await()
            if (response.hasErrors()) {
                throw ApolloException(response.errors!!.joinToString {error -> error.message})
            }
            return response.data
        } catch (e: ApolloException) {
            LOG.error (e) { "Error in GraphQl query call." }
            throw GraphQlClientException(e)
        }
    }
}