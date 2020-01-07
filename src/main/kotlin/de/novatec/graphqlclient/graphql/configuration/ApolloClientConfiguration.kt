package de.novatec.graphqlclient.graphql.configuration

import com.apollographql.apollo.ApolloClient
import de.novatec.graphqlclient.graphql.configuration.adapter.BigDecimalAdapter
import de.novatec.graphqlclient.configuration.ApplicationProperties
import de.novatec.graphqlclient.queries.type.CustomType
import okhttp3.OkHttpClient

class ApolloClientConfiguration(
    private val config: ApplicationProperties,
    private val okHttpClient: OkHttpClient,
    private val bigDecimalAdapter: BigDecimalAdapter
) {
    fun apolloClient(): ApolloClient = ApolloClient.builder()
        .serverUrl(config.serverUrl)
        .addCustomTypeAdapter(CustomType.BIGDECIMAL, bigDecimalAdapter)
        .okHttpClient(okHttpClient)
        .build()
}