package de.novatec.graphqlclient.business.graphql.repository

import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response

interface GraphQlClient {
    suspend fun <D : Operation.Data, T, V : Operation.Variables> query(query: Query<D, T, V>): T
}