package es.juandavidvega.ecc.routes

import es.juandavidvega.ecc.service.ChargesService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.chargesRouting(chargesService: ChargesService) {
    post ("/charges/eviaris-csv-import", ) {
        val multipartData = call.receiveMultipart()
        when (chargesService.import(multipartData)) {

        }

    }

}
