rootProject.name = "graphql-client"

pluginManagement {
    resolutionStrategy.eachPlugin {
        if (requested.id.namespace == "com.apollographql") {
            useModule("com.apollographql.apollo:apollo-gradle-plugin:${requested.version}")
        }
    }

    repositories {
        gradlePluginPortal()
        jcenter()
    }
}