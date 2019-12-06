package de.novatec.graphqlclient.business.graphql.configuration.adapter

import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import java.net.URI

class URIAdapter: CustomTypeAdapter<URI> {
    override fun encode(value: URI): CustomTypeValue<*> {
        return CustomTypeValue.GraphQLString(value.toString())
    }

    override fun decode(value: CustomTypeValue<*>): URI {
        return URI.create(value.value.toString())
    }
}