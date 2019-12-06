package de.novatec.graphqlclient.api

import org.springframework.web.reactive.function.server.coRouter

class GithubRoutes(private val handler: GithubHandler) {
    fun routes() = coRouter {
        "/github".nest {
            GET("/", handler::readAll)
        }
    }
}