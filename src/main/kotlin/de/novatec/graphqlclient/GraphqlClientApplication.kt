package de.novatec.graphqlclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@SpringBootApplication
class GraphqlClientApplication

fun main(args: Array<String>) {
	runApplication<GraphqlClientApplication>(*args)
}
