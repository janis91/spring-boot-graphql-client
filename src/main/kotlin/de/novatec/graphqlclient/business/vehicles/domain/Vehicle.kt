package de.novatec.graphqlclient.business.vehicles.domain

import java.math.BigDecimal

data class Vehicle(val name: String, val slug: String, val price: BigDecimal)