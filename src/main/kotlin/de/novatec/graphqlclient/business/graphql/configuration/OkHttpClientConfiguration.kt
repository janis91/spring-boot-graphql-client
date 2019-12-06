package de.novatec.graphqlclient.business.graphql.configuration

import de.novatec.graphqlclient.configuration.ApplicationProperties
import okhttp3.OkHttpClient
import org.springframework.http.HttpHeaders

class OkHttpClientConfiguration(private val config: ApplicationProperties) {
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            it.proceed(
                it.request()
                    .newBuilder()
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer ${config.githubAuthToken}")
                    .build()
            )
        }
        .build()
}