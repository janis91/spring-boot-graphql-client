package de.novatec.graphqlclient.configuration

import com.apollographql.apollo.ApolloClient
import de.novatec.graphqlclient.api.vehicles.VehiclesRoutes
import de.novatec.graphqlclient.business.vehicles.VehiclesHandler
import de.novatec.graphqlclient.business.vehicles.domain.VehiclesService
import de.novatec.graphqlclient.graphql.configuration.ApolloClientConfiguration
import de.novatec.graphqlclient.graphql.configuration.OkHttpClientConfiguration
import de.novatec.graphqlclient.graphql.configuration.adapter.BigDecimalAdapter
import de.novatec.graphqlclient.graphql.repository.GraphQlClient
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
        OkHttpClientConfiguration()
            .okHttpClient()
    }
    bean<BigDecimalAdapter>()
    bean {
        ApolloClientConfiguration(
            ref<ApplicationProperties>(),
            ref<OkHttpClient>(),
            ref<BigDecimalAdapter>()
        ).apolloClient()
    }
    bean {
        GraphQlClient(ref<ApolloClient>())
    }
    /**
     * Vehicles and Config
     */
    bean {
        VehiclesRoutes(ref<VehiclesHandler>()).routes()
    }
    bean {
        VehiclesHandler(ref<VehiclesService>())
    }
    bean {
        VehiclesService(ref<GraphQlClient>(), ref<ApplicationProperties>())
    }
}