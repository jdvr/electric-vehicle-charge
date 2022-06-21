package es.juandavidvega.ecc

import es.juandavidvega.ecc.plugins.configureDbUsingExposed
import es.juandavidvega.ecc.plugins.configureHTTP
import es.juandavidvega.ecc.plugins.configureSerialization
import es.juandavidvega.ecc.plugins.configureStaticFiles
import es.juandavidvega.ecc.routes.chargesRouting
import es.juandavidvega.ecc.routes.reportRouting
import es.juandavidvega.ecc.service.ChargesService
import es.juandavidvega.ecc.service.ReportService
import es.juandavidvega.ecc.storage.PostgresChargeStorage
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import java.util.*
import kotlin.system.exitProcess


private const val ENV_VAR_JDBC_DATABASE_URL_NAME = "JDBC_DATABASE_URL"
private const val ENV_VAR_PORT_NAME = "PORT"

fun main() {

    val config = EnvConfig()

    val envJdbcUrl = config.loadOrDefault(ENV_VAR_JDBC_DATABASE_URL_NAME, "")
    if (envJdbcUrl.isEmpty()) {
        println("Empty database url: $envJdbcUrl")
        exitProcess(0)
    }
    embeddedServer(Netty, port = getPort()) {
        configureDbUsingExposed(envJdbcUrl)
        configureSerialization()
        configureHTTP()
        configureStaticFiles()
        routing {
            val chargeStorage = PostgresChargeStorage(application.log)

            chargesRouting(ChargesService(chargeStorage))
            reportRouting(ReportService(chargeStorage))
        }
    }.start(wait = true)
}

fun getPort(): Int {
    return System.getenv(ENV_VAR_PORT_NAME)?.toInt() ?: 8080
}


class EnvConfig {
    private val dotenv: Dotenv = dotenv {
        ignoreIfMalformed = true
        ignoreIfMissing = true
    }

    fun load(key: String): Optional<String> {
        val value = this.dotenv[ENV_VAR_JDBC_DATABASE_URL_NAME] ?: System.getenv(ENV_VAR_JDBC_DATABASE_URL_NAME)
        if (value.isNullOrBlank()) {
            return Optional.empty()
        }
        return Optional.of(value)
    }

    fun loadOrDefault(key: String, defaultValue: String): String {
        return load(key).orElse(defaultValue)
    }

}
