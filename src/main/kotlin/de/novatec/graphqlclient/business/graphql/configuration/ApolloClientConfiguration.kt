package de.novatec.graphqlclient.business.graphql.configuration

import com.apollographql.apollo.ApolloClient
import de.novatec.graphqlclient.business.graphql.configuration.adapter.URIAdapter
import de.novatec.graphqlclient.configuration.ApplicationProperties
import de.novatec.graphqlclient.queries.type.CustomType
import okhttp3.OkHttpClient

class ApolloClientConfiguration(
    private val config: ApplicationProperties,
    private val okHttpClient: OkHttpClient,
    private val uriAdapter: URIAdapter
) {
    fun apolloClient(): ApolloClient = ApolloClient.builder()
        .serverUrl(config.serverUrl)
        .addCustomTypeAdapter(CustomType.URI, uriAdapter)
        .okHttpClient(okHttpClient)
        .build()
}