package es.juandavidvega.ecc.service

import es.juandavidvega.ecc.dto.Charge
import es.juandavidvega.ecc.dto.NewCharge
import es.juandavidvega.ecc.service.OperationResult.Duplicated
import es.juandavidvega.ecc.service.OperationResult.NotFound
import es.juandavidvega.ecc.storage.ChargeStorage

class ChargesService(private val chargeStorage: ChargeStorage) {
    fun create(newCharge: NewCharge): OperationResult {
        if (newCharge.priceInCent == null && newCharge.avgPriceInCent == null) {
            return Duplicated
        }

        return chargeStorage.create(
            Charge(
                newCharge.generateId(),
                newCharge.epoch,
                newCharge.durationInSeconds,
                newCharge.kw,
                newCharge.finalPrice()
            )
        )

    }

    fun delete(id: String?): OperationResult {
        if (id.isNullOrBlank()) {
            return NotFound
        }
        return chargeStorage.delete(id)
    }

    fun findAll(): Set<Charge> {
        return chargeStorage.readAll()
    }
}

fun NewCharge.generateId(): String {
    return "$epoch-$kw"
}

fun NewCharge.finalPrice(): Int = priceInCent ?: (avgPriceInCent!! * kw)
