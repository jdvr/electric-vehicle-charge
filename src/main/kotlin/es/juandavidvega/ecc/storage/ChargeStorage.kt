package es.juandavidvega.ecc.storage

import es.juandavidvega.ecc.dto.Charge
import es.juandavidvega.ecc.service.OperationResult

interface ChargeStorage {
    fun create(r: Charge): OperationResult
    fun readAll(): Set<Charge>
    fun update(r: Charge): OperationResult
    fun delete(id: String): OperationResult
}
