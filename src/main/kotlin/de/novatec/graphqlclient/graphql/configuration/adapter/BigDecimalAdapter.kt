package de.novatec.graphqlclient.graphql.configuration.adapter

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import java.math.BigDecimal
import java.net.URI

class BigDecimalAdapter: CustomTypeAdapter<BigDecimal> {
    override fun encode(value: BigDecimal): CustomTypeValue<*> {
        return CustomTypeValue.GraphQLNumber(value)
    }

    override fun decode(value: CustomTypeValue<*>): BigDecimal {
        return value.value.toString().toBigDecimal()
    }
}