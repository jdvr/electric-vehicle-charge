package es.juandavidvega.ecc.dto

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Instant
import java.util.stream.Stream

internal class ChargeShould {

    @ParameterizedTest(name = "{1} is correctly formatted")
    @MethodSource("provideChargesWithExpectedMonth") fun
    `Month are properly formatted`(charge: Charge, expectedMonth: String) {
        assertThat(charge.monthOfTheYear()).isEqualTo(expectedMonth)
    }

    companion object {
        @JvmStatic
        fun provideChargesWithExpectedMonth(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(Charge(id = "1", startedAt = instantAt("01"), duration = 893, wh = 20000, priceInCent = 300), "01/2017"),
                Arguments.of(Charge(id = "2", startedAt = instantAt("02"), duration = 893, wh = 20000, priceInCent = 300), "02/2017"),
                Arguments.of(Charge(id = "3", startedAt = instantAt("03"), duration = 893, wh = 20000, priceInCent = 300), "03/2017"),
                Arguments.of(Charge(id = "4", startedAt = instantAt("04"), duration = 893, wh = 20000, priceInCent = 300), "04/2017"),
                Arguments.of(Charge(id = "5", startedAt = instantAt("05"), duration = 893, wh = 20000, priceInCent = 300), "05/2017"),
                Arguments.of(Charge(id = "6", startedAt = instantAt("06"), duration = 893, wh = 20000, priceInCent = 300), "06/2017"),
                Arguments.of(Charge(id = "7", startedAt = instantAt("07"), duration = 893, wh = 20000, priceInCent = 300), "07/2017"),
                Arguments.of(Charge(id = "7", startedAt = instantAt("07"), duration = 893, wh = 20000, priceInCent = 300), "07/2017"),
                Arguments.of(Charge(id = "9", startedAt = instantAt("09"), duration = 893, wh = 20000, priceInCent = 300), "09/2017"),
                Arguments.of(Charge(id = "10", startedAt = instantAt("10"), duration = 893, wh = 20000, priceInCent = 300), "10/2017"),
                Arguments.of(Charge(id = "11", startedAt = instantAt("11"), duration = 893, wh = 20000, priceInCent = 300), "11/2017"),
                Arguments.of(Charge(id = "12", startedAt = instantAt("12"), duration = 893, wh = 20000, priceInCent = 300), "12/2017"),
            )
        }
    }


}

fun instantAt(monthString: String): Long =
    Instant.parse("2017-$monthString-03T10:15:30.00Z").epochSecond
