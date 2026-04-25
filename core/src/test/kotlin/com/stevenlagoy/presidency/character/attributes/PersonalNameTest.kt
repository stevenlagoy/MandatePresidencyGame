package com.stevenlagoy.presidency.character.attributes

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertNotNull

import com.stevenlagoy.presidency.characters.attributes.names.PersonalName.DisplayOption
import com.stevenlagoy.presidency.characters.attributes.names.WesternPersonalName

class PersonalNameTest {

    @Test
    fun `WHEN creating WesternPersonalName with all valid fields THEN construction succeeds and name forms are correct`() {
        val honorific = "Mr."
        val firstName = "John"
        val middleName = "Michael"
        val nickname = "Johnnie"
        val lastName = "Smith"
        val ordinal = "Sr."
        val suffixes = listOf<String>()
        val displayOptions = setOf<DisplayOption>()
        val name = WesternPersonalName(honorific, firstName, middleName, nickname, lastName, ordinal, suffixes, displayOptions)

        assertNotNull(name)
        assertEquals(honorific, name.honorific)
        assertEquals(firstName, name.firstName)
        assertEquals(middleName, name.middleName)
        assertEquals(nickname, name.nickname)
        assertEquals(lastName, name.lastName)
        assertEquals(ordinal, name.ordinal)
        assertEquals(suffixes, name.suffixes)
        assertEquals(displayOptions, name.displayOptions)

        assertEquals("John Michael Smith", name.legalName)
        assertEquals("Mr. John Michael Smith Sr.", name.formalName)
        assertEquals("Mr. John Michael \"Johnnie\" Smith Sr.", name.biographicalName)
        assertEquals("John Michael Smith Sr.", name.commonName)
        assertEquals("Johnnie Smith", name.informalName)
        assertEquals("Smith, John Michael", name.indexedName)
    }

}
