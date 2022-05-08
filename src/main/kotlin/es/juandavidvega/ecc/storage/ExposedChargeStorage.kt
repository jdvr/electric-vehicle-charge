package es.juandavidvega.ecc.storage

import es.juandavidvega.ecc.dto.Charge
import es.juandavidvega.ecc.service.OperationResult
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger

object ChargeEntity : IntIdTable() {
    val chargeId: Column<String> = varchar("charge_id", 50).uniqueIndex()
    val startedAt: Column<Long> = long("started_at")
    val duration: Column<Int> = integer("duration_in_sec")
    val kw: Column<Int> = integer("kw")
    val priceInCent: Column<Int> = integer("price_in_cent")
}

class PostgresChargeStorage(private val log: Logger) : ChargeStorage {

    override fun create(r: Charge): OperationResult = try {
        transaction {
            ChargeEntity.insert {
                it[chargeId] = r.id
                it[startedAt] = r.startedAt
                it[kw] = r.kw
                it[duration] = r.duration
                it[priceInCent] = r.priceInCent
            }
        }
        OperationResult.Success
    } catch (e: java.lang.Exception) {
        log.error("Failed to insert charge", e)
        OperationResult.GenericError
    }

    override fun readAll(): Set<Charge> {
        return transaction {
            ChargeEntity.selectAll().map {
                Charge(
                    it[ChargeEntity.chargeId],
                    it[ChargeEntity.startedAt],
                    it[ChargeEntity.duration],
                    it[ChargeEntity.kw],
                    it[ChargeEntity.priceInCent],
                )
            }.toSet()
        }

    }

    override fun update(r: Charge): OperationResult {
        TODO("Not yet implemented")
    }

    override fun delete(id: String): OperationResult = try {
        transaction {
            ChargeEntity.deleteWhere { ChargeEntity.chargeId eq id }
        }
        OperationResult.Success
    } catch (e: java.lang.Exception) {
        log.error("Failed to delete charge with id: $id", e)
        OperationResult.GenericError
    }


}
