package es.juandavidvega.ecc.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import es.juandavidvega.ecc.storage.ChargeEntity
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.system.exitProcess

fun Application.configureDbUsingExposed(envJdbcUrl: String) {
    val datasourceConfig = HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = envJdbcUrl
        maximumPoolSize = 3
        isAutoCommit = true
        validate()
    }

    try {
        Database.connect(HikariDataSource(datasourceConfig))

        transaction {
            addLogger(Slf4jSqlDebugLogger)
            SchemaUtils.create(ChargeEntity)
        }
    } catch (e: java.lang.Exception) {
        this.log.error("Failed to init db", e)
    }
}
