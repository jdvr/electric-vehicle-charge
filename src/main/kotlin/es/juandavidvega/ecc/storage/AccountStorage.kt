package es.juandavidvega.ecc.storage

import es.juandavidvega.ecc.service.OperationResult

interface AccountStorage {
    fun create(userName: String): OperationResult
}
