package es.juandavidvega.ecc.storage

import es.juandavidvega.ecc.dto.Charge

interface ChargesStorage {
    fun create(r: Charge)
    fun readAll(): Set<Charge>
    fun update(r: Charge)
    fun delete(r: Charge)
}
