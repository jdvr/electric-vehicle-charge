package es.juandavidvega.ecc.routes

import es.juandavidvega.ecc.service.PeriodQuery
import es.juandavidvega.ecc.service.ReportService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.reportRouting(reportService: ReportService) {
    route("/report") {
        get("/breakdown") {
            val periodsBreakdown = reportService.periodsBreakdown(PeriodQuery(resolution = PeriodQuery.Resolution.Month))
            call.respond(periodsBreakdown)
        }
    }

}
