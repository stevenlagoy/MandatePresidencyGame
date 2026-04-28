package com.stevenlagoy.presidency.util

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.isRegularFile

class IOUtilsTest {

    @Test
    fun `WHEN listing files THEN some files are listed`() {
        assertNotNull(Path.of("").listFiles())
        assertTrue { Path.of("").listFiles().isNotEmpty() }
        assertTrue { Path.of("").listFiles().all { it.isRegularFile() } }
    }

}
