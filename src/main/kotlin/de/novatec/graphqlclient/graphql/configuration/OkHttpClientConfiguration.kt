package de.novatec.graphqlclient.graphql.configuration

import de.novatec.graphqlclient.configuration.ApplicationProperties
import okhttp3.OkHttpClient
import org.springframework.http.HttpHeaders

class OkHttpClientConfiguration {
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()
}