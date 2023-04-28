package es.juandavidvega.ecc.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.CORS


fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Head)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHost("localhost:8080",  schemes = listOf("http", "https"))
        allowHost("localhost:3000", schemes = listOf("http", "https"))
        allowHost("*.juandavidvega.es", listOf("http", "https"))
        allowHost("juandavidvega.es", listOf("http", "https"))
        allowHeader(HttpHeaders.ContentType)
    }

}
