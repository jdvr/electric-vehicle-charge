package es.juandavidvega.ecc.routes

import es.juandavidvega.ecc.service.PeriodQuery
import es.juandavidvega.ecc.service.PeriodsBreakdownService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.reportRouting(periodsBreakdownService: PeriodsBreakdownService) {
    route("/report") {
        get("/breakdown") {
            val periodsBreakdown = periodsBreakdownService.periodsBreakdown(PeriodQuery(resolution = PeriodQuery.Resolution.Month))
            call.respond(periodsBreakdown)
        }
    }

}
