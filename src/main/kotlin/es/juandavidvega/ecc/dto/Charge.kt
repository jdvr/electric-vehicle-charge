package es.juandavidvega.ecc.dto

import kotlinx.serialization.Serializable


@Serializable
data class Charge(val id: String, val epoch: Long, val kw: Int, val priceInCent: Int)

@Serializable
data class NewRecharge(val epoch: Long, val kw: Int, val avgPriceInCent: Int?, val priceInCent: Int?)
