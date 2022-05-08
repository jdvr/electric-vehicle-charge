package es.juandavidvega.ecc

import es.juandavidvega.ecc.plugins.configureDbUsingExposed
import es.juandavidvega.ecc.plugins.configureHTTP
import es.juandavidvega.ecc.plugins.configureSerialization
import es.juandavidvega.ecc.routes.chargesRouting
import es.juandavidvega.ecc.service.ChargesService
import es.juandavidvega.ecc.storage.PostgresChargeStorage
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*


fun main() {


    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureDbUsingExposed()
        configureSerialization()
        configureHTTP()
        routing {
            chargesRouting(ChargesService(PostgresChargeStorage(application.log)))
        }
    }.start(wait = true)
}
