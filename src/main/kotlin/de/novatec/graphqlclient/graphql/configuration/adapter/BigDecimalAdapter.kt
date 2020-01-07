package de.novatec.graphqlclient.graphql.configuration.adapter

import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
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