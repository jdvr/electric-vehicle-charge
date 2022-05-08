package es.juandavidvega.ecc.routes

enum class Code {
    CLIENT_ERROR,
    SERVER_ERROR
}

data class ErrorResponse(val code: Code, val message: String)
data class SuccessResponse(val message: String)
