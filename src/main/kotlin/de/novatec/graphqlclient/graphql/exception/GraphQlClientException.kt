package de.novatec.graphqlclient.graphql.exception

class GraphQlClientException : Exception {
    constructor()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}