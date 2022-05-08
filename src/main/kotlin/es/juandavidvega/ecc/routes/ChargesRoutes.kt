package es.juandavidvega.ecc.routes

import es.juandavidvega.ecc.dto.NewCharge
import es.juandavidvega.ecc.service.ChargesService
import es.juandavidvega.ecc.service.OperationResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.chargesRouting(chargesService: ChargesService) {
    post("/charge") {
        val newCharge = call.receive<NewCharge>()
        when (chargesService.create(newCharge)) {
            OperationResult.Duplicated -> call.respond(
                status = HttpStatusCode.Conflict,
                message = ErrorResponse(Code.CLIENT_ERROR, "Duplicate charge")
            )
            OperationResult.Success -> call.respond(
                status = HttpStatusCode.Created,
                message = SuccessResponse("Created")
            )
            else -> call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ErrorResponse(Code.SERVER_ERROR, "Unkown error")
            )
        }
    }

    delete("/charge/{id}") {
        val id = call.parameters["id"]
        when (chargesService.delete(id)) {
            OperationResult.GenericError -> call.respond(
                status = HttpStatusCode.NotFound,
                message = ErrorResponse(Code.SERVER_ERROR, "Error while deleting: $id")
            )
            else -> call.respond(status = HttpStatusCode.Accepted, message = SuccessResponse("Deleted"))
        }
    }

    get("/charge") {
        val changes = chargesService.findAll()
        call.respond(status = HttpStatusCode.OK, message = changes)
    }
}
