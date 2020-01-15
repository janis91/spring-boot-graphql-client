package de.novatec.graphqlclient.graphql.test

import com.github.tomakehurst.wiremock.client.WireMock.*
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.util.UriComponentsBuilder

class CMSGraphqlWireMockStubber(private val serverUrl: String) {
    fun stubVehicles() {
        stubFor(
            post(urlEqualTo(UriComponentsBuilder.fromHttpUrl(serverUrl).build().toUri().path))
                .withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson("""{
                        "operationName": "Vehicles",
                        "variables": {},
                        "query": "query Vehicles { nodes(filter: {schema: {is: vehicle}}) { __typename elements { __typename ... on vehicle { fields { __typename name price } } } } }"
                    }"""))
                .willReturn(okJson("""
                    {
                      "data": {
                        "nodes": {
                          "__typename": "NodesPage",
                          "elements": [
                            {
                              "__typename": "vehicle",
                              "fields": {
                                "__typename": "vehicleFields",
                                "name": "Gulfstream G550",
                                "price": 42000000
                              }
                            },
                            {
                              "__typename": "vehicle",
                              "fields": {
                                "__typename": "vehicleFields",
                                "name": "Maize Blue Solar Car",
                                "price": 185000.12
                              }
                            }
                          ]
                        }
                      }
                    }
                """))
        )
    }
}
