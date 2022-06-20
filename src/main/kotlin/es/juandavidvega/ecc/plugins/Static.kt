package es.juandavidvega.ecc.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File


fun Application.configureStaticFiles() {
    routing {
        static {
            staticRootFolder = File("static")
            files(".")
            default("index.html")

        }
    }

}
