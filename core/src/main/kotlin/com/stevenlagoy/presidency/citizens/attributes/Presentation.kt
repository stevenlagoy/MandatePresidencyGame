package com.stevenlagoy.presidency.citizens.attributes

import com.stevenlagoy.presidency.util.normalize
import com.sun.tools.javac.util.StringUtils

enum class Presentation(val label: String) {
    MAN("Man"),
    WOMAN("Woman"),
    NONBINARY("Non-Binary");

    companion object {
        fun fromLabel(label: String) = entries.first {
            it.label.normalize().equals(label.normalize(), ignoreCase = true)
        }
    }
}
