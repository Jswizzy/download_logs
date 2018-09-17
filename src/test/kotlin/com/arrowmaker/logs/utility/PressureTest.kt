package com.arrowmaker.logs.utility

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PressureTest {
    @Test fun `Convert Pressure`() {
        val given = 4_191_629
        val expected = -83.15
        val pressure = Pressure.from(given)

        println(pressure)

        assertTrue(expected >= pressure - 1 && expected <= pressure + 1)
    }
}