package es.juandavidvega.ecc

import es.juandavidvega.ecc.dto.Charge
import es.juandavidvega.ecc.plugins.configureHTTP
import es.juandavidvega.ecc.plugins.configureSerialization
import es.juandavidvega.ecc.routes.chargesRouting
import es.juandavidvega.ecc.service.ChargesService
import es.juandavidvega.ecc.storage.ChargesStorage
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*


class InMemoryChargesStorage: ChargesStorage {
    private val charges = mutableListOf<Charge>()
    override fun create(r: Charge) {
        TODO("Not yet implemented")
    }

    override fun readAll(): Set<Charge> {
        TODO("Not yet implemented")
    }

    override fun update(r: Charge) {
        TODO("Not yet implemented")
    }

    override fun delete(r: Charge) {
        TODO("Not yet implemented")
    }

}

fun main() {

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        configureHTTP()
        routing {
            chargesRouting(ChargesService(InMemoryChargesStorage()))
        }
    }.start(wait = true)
}
