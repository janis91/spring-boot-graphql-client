package de.novatec.graphqlclient.api

import java.net.URI

data class Repo(val name: String, val url: URI, val description: String) {
}