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
                newCharge.wh,
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
            .sortedByDescending { it.startedAt }
            .toSet()
    }
}

fun NewCharge.generateId(): String {
    return "$epoch-$wh"
}

fun NewCharge.finalPrice(): Int {
    if (priceInCent != null) {
        return priceInCent
    }

    val whPrice = avgPriceInCent!! / 1000.toDouble()
    return (wh * whPrice).toInt()
}
