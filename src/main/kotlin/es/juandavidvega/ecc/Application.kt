package es.juandavidvega.ecc

import es.juandavidvega.ecc.plugins.configureDbUsingExposed
import es.juandavidvega.ecc.plugins.configureHTTP
import es.juandavidvega.ecc.plugins.configureSerialization
import es.juandavidvega.ecc.plugins.configureStaticFiles
import es.juandavidvega.ecc.routes.chargesRouting
import es.juandavidvega.ecc.service.ChargesService
import es.juandavidvega.ecc.storage.PostgresChargeStorage
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*


fun main() {


    embeddedServer(Netty, port = getPort()) {
        configureDbUsingExposed()
        configureSerialization()
        configureHTTP()
        configureStaticFiles()
        routing {
            chargesRouting(ChargesService(PostgresChargeStorage(application.log)))
        }
    }.start(wait = true)
}


fun getPort(): Int {
    return System.getenv("PORT")?.toInt() ?: 8080
}
