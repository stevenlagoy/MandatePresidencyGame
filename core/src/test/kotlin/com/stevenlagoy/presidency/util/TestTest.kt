package com.stevenlagoy.presidency.util

import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.string.shouldHaveLength

val MyTestSuite by testSuite {
    test("string length") {
        "Test me!".shouldHaveLength(8)
    }
}
