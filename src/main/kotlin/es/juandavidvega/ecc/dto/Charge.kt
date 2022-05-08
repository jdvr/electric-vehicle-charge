package es.juandavidvega.ecc.dto

import kotlinx.serialization.Serializable

typealias Seconds = Int

@Serializable
data class Charge(
    val id: String,
    val startedAt: Long,
    val duration: Seconds,
    val kw: Int,
    val priceInCent: Int
)

@Serializable
data class NewCharge(
    val epoch: Long,
    val kw: Int,
    val durationInSeconds: Int,
    val avgPriceInCent: Int? = null,
    val priceInCent: Int? = null
)
