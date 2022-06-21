package es.juandavidvega.ecc.dto

import kotlinx.serialization.Serializable

@Serializable
data class Period(val key: String, val totalPriceInCents: Int, val totalWh: Int) {
    operator fun plus(other: Period): Period =
        Period(
            key = key,
            totalPriceInCents = totalPriceInCents + other.totalPriceInCents,
            totalWh = totalWh + other.totalWh
        )
}

@Serializable
data class PeriodsBreakdown(val periods: List<Period>)
