package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic

enum class Education(val value: Int) : Jsonic<Education> {

    /**
     * `0`:
     * Early Childhood Education: For children younger than three years through Preschool or Kindergarten.
     */
    EARLY_CHILDHOOD(0),

    /**
     * `1`:
     * Primary Education: Core knowledge in Reading, Writing, and Mathematics. Elementary School (K/1 - 5/6).
     */
    PRIMARY(1),

    /**
     * `2`:
     * Lower Secondary Education: English, Science, Social Studies, and Algebra. Middle School (5/6 - 8/9)
     */
    LOWER_SECONDARY(2),

    /**
     * `3`:
     * Upper Secondary Education: Language, Political Science, Higher Sciences, Higher Mathematics. High School (9 - 12)
     */
    UPPER_SECONDARY(3),

    /**
     * `4`:
     * Post-Secondary Non-Tertiary Education: Vocational or Technical Skills, Certifications.
     */
    POST_SECONDARY(4),

    /**
     * `5`:
     * Short-Cycle Tertiary Education: Any amount of a practically-based occupationally-specific concentrated education program.
     */
    SHORT_TERTIARY(5),

    /**
     * `6`:
     * Bachelor's Degree or Equivalent: Intermediate academic or professional knowledge, skills, and competencies.
     */
    BACHELORS(6),

    /**
     * `7`:
     * Master's Degree or Equivalent: Advanced academic or professional knowledge, skills, and competencies.
     */
    MASTERS(7),

    /**
     * `8`:
     * Doctorate or Equivalent: Advanced research qualification, usually with submission and defense of a substantive dissertation.
     */
    DOCTORAL(8);

    companion object {
        private val BY_VALUE = entries.associateBy(Education::value)
        @JvmStatic
        fun fromValue(value: Int): Education? = BY_VALUE[value]
    }

    fun educationFromString(name: String): Education? =
        entries.find { it.name.equals(name, ignoreCase = true) }

    override fun toJson() = JSONObject("education", value)

    override fun fromJson(json: JSONObject): Education? {
        return Education.fromValue(json.get("education") as Int)
    }
}
