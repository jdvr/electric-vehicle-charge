package es.juandavidvega.ecc.routes

import kotlinx.serialization.Serializable

enum class Code {
    CLIENT_ERROR,
    SERVER_ERROR
}

@Serializable
data class ErrorResponse(val code: Code, val message: String)

@Serializable
data class SuccessResponse(val message: String)
