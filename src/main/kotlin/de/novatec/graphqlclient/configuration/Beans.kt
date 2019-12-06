package de.novatec.graphqlclient.configuration

import com.apollographql.apollo.ApolloClient
import de.novatec.graphqlclient.api.GithubHandler
import de.novatec.graphqlclient.api.GithubRoutes
import de.novatec.graphqlclient.configuration.ApplicationProperties
import de.novatec.graphqlclient.business.graphql.repository.GraphQlClient
import de.novatec.graphqlclient.business.graphql.repository.GraphQlClientImpl
import de.novatec.graphqlclient.business.graphql.configuration.ApolloClientConfiguration
import de.novatec.graphqlclient.business.graphql.configuration.OkHttpClientConfiguration
import de.novatec.graphqlclient.business.graphql.configuration.adapter.URIAdapter
import okhttp3.OkHttpClient
import org.springframework.context.support.beans

fun beans() = beans {
    /**
     * Application and Config
     */
    bean<ApplicationProperties>()
    /**
     * GraphQL and Config
     */
    bean {
        OkHttpClientConfiguration(ref<ApplicationProperties>())
            .okHttpClient()
    }
    bean<URIAdapter>()
    bean {
        ApolloClientConfiguration(
            ref<ApplicationProperties>(),
            ref<OkHttpClient>(),
            ref<URIAdapter>()
        ).apolloClient()
    }
    bean<GraphQlClient> {
        GraphQlClientImpl(ref<ApolloClient>())
    }
    /**
     * Github and Config
     */
    bean {
        GithubRoutes(ref<GithubHandler>()).routes()
    }
    bean {
        GithubHandler(ref<GraphQlClient>())
    }
}