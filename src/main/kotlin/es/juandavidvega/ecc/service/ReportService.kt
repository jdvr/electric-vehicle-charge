package es.juandavidvega.ecc.service

import es.juandavidvega.ecc.dto.Period
import es.juandavidvega.ecc.dto.PeriodsBreakdown
import es.juandavidvega.ecc.storage.ChargeStorage


data class PeriodQuery(val resolution: Resolution) {
    enum class Resolution {
        Month
    }
}

class ReportService(private val chargeStorage: ChargeStorage) {
    fun periodsBreakdown(periodQuery: PeriodQuery): PeriodsBreakdown {
        val charges = chargeStorage.readAll()
        val periods = charges
            .map {
                Period(
                    key = it.monthOfTheYear(),
                    totalWh = it.wh,
                    totalPriceInCents = it.priceInCent
                )
            }.fold(mutableMapOf<String, Period>()) { periodByKey, period ->
                val current = periodByKey[period.key] ?: Period(period.key, totalPriceInCents = 0, totalWh = 0)
                periodByKey[period.key] = current + period
                periodByKey
            }
            .values
            .sortedBy { it.key }
            .toList()
        return PeriodsBreakdown(periods)
    }
}
