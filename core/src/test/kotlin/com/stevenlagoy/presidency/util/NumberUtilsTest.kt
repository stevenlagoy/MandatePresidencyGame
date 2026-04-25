package com.stevenlagoy.presidency.util

import org.junit.jupiter.api.Test

class NumberUtilsTest {

    @Test
    fun `WHEN converting numbers less than 100 to words THEN word representations are correct`() {

        for (i in 0..100) {
            println(i.toWords())
        }

    }


}
