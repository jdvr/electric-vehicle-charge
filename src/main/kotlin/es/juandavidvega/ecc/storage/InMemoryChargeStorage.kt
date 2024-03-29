package es.juandavidvega.ecc.storage

import es.juandavidvega.ecc.dto.Charge
import es.juandavidvega.ecc.service.OperationResult
import io.ktor.util.logging.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryChargeStorage(log: Logger) : ChargeStorage {
    init {
        log.debug("Using InMemory storage, all data will be lost after a restart")
    }

    private var charges: MutableMap<String, Charge> = ConcurrentHashMap()

    override fun create(r: Charge): OperationResult {
        charges[r.id] = r
        return OperationResult.Success
    }

    override fun readAll(): Set<Charge> =
        charges.values.toMutableSet()


    override fun update(r: Charge): OperationResult =
        if (!charges.contains(r.id)) {
            OperationResult.NotFound
        } else {
            charges[r.id] = r
            OperationResult.Success
        }

    override fun delete(id: String): OperationResult =
        if (!charges.contains(id)) {
            OperationResult.NotFound
        } else {
            charges.remove(id)
            OperationResult.Success
        }
}
