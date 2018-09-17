package com.arrowmaker.logs.utility

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CoordinateTest {
    @Test
    fun `Negative DMS to Degree Decimal`() {
        val dms = -8423.40
        val expectedDd = -84.39

        val dd = Coordinate.dmsToDd(dms)

        assertTrue(dd <= expectedDd + 1 && dd >= expectedDd - 1)
    }

    @Test
    fun `DMS to Degree Decimal`() {
        val dms = 3346.32
        val expectedDd = 33.772

        val dd = Coordinate.dmsToDd(dms)

        assertTrue(dd <= expectedDd + 1 && dd >= expectedDd - 1)
    }
}