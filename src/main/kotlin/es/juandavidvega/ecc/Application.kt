package es.juandavidvega.ecc

import es.juandavidvega.ecc.plugins.configureDbUsingExposed
import es.juandavidvega.ecc.plugins.configureHTTP
import es.juandavidvega.ecc.plugins.configureSerialization
import es.juandavidvega.ecc.plugins.configureStaticFiles
import es.juandavidvega.ecc.routes.chargesRouting
import es.juandavidvega.ecc.routes.reportRouting
import es.juandavidvega.ecc.service.ChargesService
import es.juandavidvega.ecc.service.PeriodsBreakdownService
import es.juandavidvega.ecc.storage.InMemoryChargeStorage
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
private const val ENV_VAR_STATELESS = "STATELESS"
private const val ENV_VAR_PORT_NAME = "PORT"

fun main() {
    embeddedServer(
        Netty,
        port = getPort(),
        module = Application::myApplicationModule
    ).start(wait = true)
}

fun Application.myApplicationModule() {
    val config = EnvConfig()
    val stateless = config.loadOrDefault(ENV_VAR_STATELESS, false)

    val envJdbcUrl = config.loadOrDefault(ENV_VAR_JDBC_DATABASE_URL_NAME, "")
    if (envJdbcUrl.isEmpty() && !stateless) {
        println("Empty database url: $envJdbcUrl")
        exitProcess(0)
    }

    if (!stateless) {
        configureDbUsingExposed(envJdbcUrl)
    }

    configureSerialization()
    configureHTTP()
    configureStaticFiles()
    routing {
        val chargeStorage = if (stateless) {
            PostgresChargeStorage(application.log)
        } else {
            InMemoryChargeStorage()
        }

        chargesRouting(ChargesService(chargeStorage))
        reportRouting(PeriodsBreakdownService(chargeStorage))
    }
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

    inline fun <reified T> loadOrDefault(key: String, defaultValue: T): T =
        load(key).map { it as T }.orElse(defaultValue)



}
