package de.novatec.graphqlclient.api

import com.apollographql.apollo.api.Input
import de.novatec.graphqlclient.business.graphql.repository.GraphQlClient
import de.novatec.graphqlclient.queries.SearchForRepositoriesQuery
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.lang.IllegalArgumentException
import java.util.stream.Collectors

private val LOG = KotlinLogging.logger {}

class GithubHandler(private val client: GraphQlClient) {
    suspend fun readAll(request: ServerRequest): ServerResponse {
        val query =
            request.queryParam("query").orElseThrow { IllegalArgumentException("Query parameter 'query' is needed") }
        LOG.debug { "GET first repository from github repsoritory search for query string '${query}'" }
        val response = client.query(
            SearchForRepositoriesQuery(
                query,
                Input.fromNullable(1)
            )
        ).search.nodes!!.stream().map {
            (it!!.inlineFragment as? SearchForRepositoriesQuery.AsRepository)!!
        }
            .map { Repo(it.name, it.url, it.description!!) }
            .collect(Collectors.toList())
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(response)
    }
}
