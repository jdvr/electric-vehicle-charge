package es.juandavidvega.ecc.dto

import io.ktor.server.util.*
import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.Instant

typealias Seconds = Int

@Serializable
data class Charge(
    val id: String,
    val startedAt: Long,
    val duration: Seconds,
    val wh: Int,
    val priceInCent: Int
) {
    fun monthOfTheYear(): String{
        val formatter = SimpleDateFormat("MM/YYYY")
        return formatter.format(Instant.ofEpochSecond(startedAt).toGMTDate().toJvmDate())
    }
}

@Serializable
data class NewCharge(
    val epoch: Long,
    val wh: Int,
    val durationInSeconds: Int,
    val avgPriceInCent: Int? = null,
    val priceInCent: Int? = null
)
