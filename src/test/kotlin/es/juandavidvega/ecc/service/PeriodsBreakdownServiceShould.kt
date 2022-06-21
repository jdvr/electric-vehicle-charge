package es.juandavidvega.ecc.service

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import es.juandavidvega.ecc.dto.Charge
import es.juandavidvega.ecc.dto.Period
import es.juandavidvega.ecc.storage.ChargeStorage
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.time.Instant

internal class PeriodsBreakdownServiceShould {

    @Test fun
    `Aggregate data by month including cost and consumed wh`(){
        val may = Instant.parse("2022-05-03T10:15:30.00Z")
        val april = Instant.parse("2022-04-03T10:15:30.00Z")
        val march = Instant.parse("2022-03-03T10:15:30.00Z")
        val charges = listOf(
            Charge(id = "1", startedAt = may.epochSecond, duration = 123, wh = 20000, priceInCent = 300),
            Charge(id = "2", startedAt = may.epochSecond, duration = 123, wh = 20000, priceInCent = 300),
            Charge(id = "3", startedAt = april.epochSecond, duration = 123, wh = 30000, priceInCent = 400),
            Charge(id = "4", startedAt = april.epochSecond, duration = 123, wh = 30000, priceInCent = 400),
            Charge(id = "5", startedAt = march.epochSecond, duration = 123, wh = 40000, priceInCent = 500),
            Charge(id = "6", startedAt = march.epochSecond, duration = 123, wh = 40000, priceInCent = 500),
        )


        val chargeStorageMock = mockk<ChargeStorage>()

        every { chargeStorageMock.readAll() } returns charges.toSet()

        val reportService = ReportService(chargeStorageMock)

        val periodsBreakdown = reportService.periodsBreakdown(PeriodQuery(
            resolution = PeriodQuery.Resolution.Month
        ))

        assertThat(periodsBreakdown.periods)
            .isEqualTo(listOf(
                Period(key = "03/2022", totalPriceInCents = 1000, totalWh = 80000),
                Period(key = "04/2022", totalPriceInCents = 800, totalWh = 60000),
                Period(key = "05/2022", totalPriceInCents = 600, totalWh = 40000),
            ))
    }

    @Test fun
    `Propagate empty list if there is not charges`(){
        val chargeStorageMock = mockk<ChargeStorage>()

        every { chargeStorageMock.readAll() } returns emptySet()

        val reportService = ReportService(chargeStorageMock)

        val periodsBreakdown = reportService.periodsBreakdown(PeriodQuery(
            resolution = PeriodQuery.Resolution.Month
        ))

        assertThat(periodsBreakdown.periods).isEmpty()
    }
}

