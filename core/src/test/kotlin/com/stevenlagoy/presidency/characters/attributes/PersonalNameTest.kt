package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.presidency.characters.attributes.names.EasternPersonalName
import com.stevenlagoy.presidency.characters.attributes.names.HispanicPersonalName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertNotNull

import com.stevenlagoy.presidency.characters.attributes.names.PersonalName.DisplayOption
import com.stevenlagoy.presidency.characters.attributes.names.WesternPersonalName

class PersonalNameTest {

    @Test
    fun `WHEN creating WesternPersonalName with all valid fields THEN construction succeeds and name forms are correct`() {
        val firstName = "John"
        val middleName = "Michael"
        val lastName = "Smith"
        val honorific = "Mr."
        val nickname = "Johnnie"
        val ordinal = "Sr."
        val suffixes = listOf("MD")
        val displayOptions = setOf<DisplayOption>()
        val name = WesternPersonalName(firstName, middleName, lastName, ordinal, honorific, nickname, suffixes, displayOptions)

        assertNotNull(name)

        assertEquals(honorific, name.honorific)
        assertEquals(firstName, name.firstName)
        assertEquals(middleName, name.middleName)
        assertEquals(nickname, name.nickname)
        assertEquals(lastName, name.lastName)
        assertEquals(ordinal, name.ordinal)
        assertEquals(suffixes, name.suffixes)
        assertEquals(displayOptions, name.displayOptions)

        assertEquals("John Michael Smith Sr.", name.legalName)
        assertEquals("Mr. John Michael Smith Sr., MD", name.formalName)
        assertEquals("Mr. John Michael \"Johnnie\" Smith Sr., MD", name.biographicalName)
        assertEquals("John Michael Smith Sr.", name.commonName)
        assertEquals("Johnnie Smith", name.informalName)
        assertEquals("Smith, John Michael", name.indexedName)
        assertEquals("JMS", name.initials)
    }

    @Test
    fun `WHEN creating minimal WesternPersonalName THEN construction succeeds and name forms are correct`() {
        val firstName = "John"
        val middleName = "Michael"
        val lastName = "Smith"
        val name = WesternPersonalName(firstName, middleName, lastName)

        assertNotNull(name)

        assertEquals(firstName, name.firstName)
        assertEquals(middleName, name.middleName)
        assertEquals(lastName, name.lastName)

        assertEquals("John Michael Smith", name.legalName)
        assertEquals("John Michael Smith", name.formalName)
        assertEquals("John Michael Smith", name.biographicalName)
        assertEquals("John Michael Smith", name.commonName)
        assertEquals("John Smith", name.informalName)
        assertEquals("Smith, John Michael", name.indexedName)
        assertEquals("JMS", name.initials)
    }

    @Test
    fun `WHEN using the copy constructor on a WesternPersonalName THEN all fields are copied properly`() {
        val firstName = "John"
        val middleName = "Michael"
        val lastName = "Smith"
        val honorific = "Mr."
        val nickname = "Johnnie"
        val ordinal = "Sr."
        val suffixes = listOf("MD")
        val displayOptions = setOf<DisplayOption>()

        val original = WesternPersonalName(firstName, middleName, lastName, ordinal, honorific, nickname, suffixes, displayOptions)
        val copied = WesternPersonalName(original)

        assertNotNull(copied)

        assertEquals(honorific, copied.honorific)
        assertEquals(firstName, copied.firstName)
        assertEquals(middleName, copied.middleName)
        assertEquals(nickname, copied.nickname)
        assertEquals(lastName, copied.lastName)
        assertEquals(ordinal, copied.ordinal)
        assertEquals(suffixes, copied.suffixes)
        assertEquals(displayOptions, copied.displayOptions)

        assertEquals("John Michael Smith Sr.", copied.legalName)
        assertEquals("Mr. John Michael Smith Sr., MD", copied.formalName)
        assertEquals("Mr. John Michael \"Johnnie\" Smith Sr., MD", copied.biographicalName)
        assertEquals("John Michael Smith Sr.", copied.commonName)
        assertEquals("Johnnie Smith", copied.informalName)
        assertEquals("Smith, John Michael", copied.indexedName)
        assertEquals("JMS", copied.initials)
    }

    @Test
    fun `WHEN converting WesternPersonalName to json representation and back to WesternPersonalName THEN fields are preserved and restored properly`() {
        val firstName = "John"
        val middleName = "Michael"
        val lastName = "Smith"
        val honorific = "Mr."
        val nickname = "Johnnie"
        val ordinal = "Sr."
        val suffixes = listOf("MD")
        val displayOptions = setOf<DisplayOption>()

        val original = WesternPersonalName(firstName, middleName, lastName, ordinal, honorific, nickname, suffixes, displayOptions)
        val reconstructed = WesternPersonalName(original.toJson())

        assertNotNull(reconstructed)

        assertEquals(honorific, reconstructed.honorific)
        assertEquals(firstName, reconstructed.firstName)
        assertEquals(middleName, reconstructed.middleName)
        assertEquals(nickname, reconstructed.nickname)
        assertEquals(lastName, reconstructed.lastName)
        assertEquals(ordinal, reconstructed.ordinal)
        assertEquals(suffixes, reconstructed.suffixes)
        assertEquals(displayOptions, reconstructed.displayOptions)

        assertEquals("John Michael Smith Sr.", reconstructed.legalName)
        assertEquals("Mr. John Michael Smith Sr., MD", reconstructed.formalName)
        assertEquals("Mr. John Michael \"Johnnie\" Smith Sr., MD", reconstructed.biographicalName)
        assertEquals("John Michael Smith Sr.", reconstructed.commonName)
        assertEquals("Johnnie Smith", reconstructed.informalName)
        assertEquals("Smith, John Michael", reconstructed.indexedName)
        assertEquals("JMS", reconstructed.initials)
    }

    @Test
    fun `WHEN creating HispanicPersonalName with all valid fields THEN construction succeeds and name forms are correct`() {
        val givenName = "Juan"
        val paternalName = "Martínez"
        val maternalName = "López"
        val honorific = "Dr."
        val nickname = "Jano"
        val suffixes = listOf("PhD")
        val displayOptions = setOf<DisplayOption>()
        val name = HispanicPersonalName(givenName, paternalName, maternalName, honorific, nickname, suffixes, displayOptions)

        assertNotNull(name)

        assertEquals(honorific, name.honorific)
        assertEquals(givenName, name.givenName)
        assertEquals(nickname, name.nickname)
        assertEquals(paternalName, name.paternalName)
        assertEquals(maternalName, name.maternalName)
        assertEquals(suffixes, name.suffixes)
        assertEquals(displayOptions, name.displayOptions)
        assertEquals("Martínez López", name.apellidos)

        assertEquals("Juan Martínez López", name.legalName)
        assertEquals("Dr. Juan Martínez López, PhD", name.formalName)
        assertEquals("Dr. Juan \"Jano\" Martínez López, PhD", name.biographicalName)
        assertEquals("Jano Martínez López", name.commonName)
        assertEquals("Jano Martínez López", name.informalName)
        assertEquals("Martínez López, Juan", name.indexedName)
        assertEquals("JML", name.initials)
    }

    @Test
    fun `WHEN creating minimal HispanicPersonalName THEN construction succeeds and name forms are correct`() {
        val givenName = "Juan"
        val paternalName = "Martínez"
        val maternalName = "López"
        val name = HispanicPersonalName(givenName, paternalName, maternalName)

        assertNotNull(name)

        assertEquals(givenName, name.givenName)
        assertEquals(paternalName, name.paternalName)
        assertEquals(maternalName, name.maternalName)
        assertEquals("$paternalName $maternalName", name.apellidos)

        assertEquals("Juan Martínez López", name.legalName)
        assertEquals("Juan Martínez López", name.formalName)
        assertEquals("Juan Martínez López", name.biographicalName)
        assertEquals("Juan Martínez López", name.commonName)
        assertEquals("Juan Martínez López", name.informalName)
        assertEquals("Martínez López, Juan", name.indexedName)
        assertEquals("JML", name.initials)
    }

    @Test
    fun `WHEN using the copy constructor on a HispanicPersonalName THEN all fields are copied properly`() {
        val givenName = "Juan"
        val paternalName = "Martínez"
        val maternalName = "López"
        val honorific = "Dr."
        val nickname = "Jano"
        val suffixes = listOf("PhD")
        val displayOptions = setOf<DisplayOption>()

        val original = HispanicPersonalName(givenName, paternalName, maternalName, honorific, nickname, suffixes, displayOptions)
        val copied = HispanicPersonalName(original)

        assertNotNull(copied)

        assertEquals(honorific, copied.honorific)
        assertEquals(givenName, copied.givenName)
        assertEquals(nickname, copied.nickname)
        assertEquals(paternalName, copied.paternalName)
        assertEquals(maternalName, copied.maternalName)
        assertEquals(suffixes, copied.suffixes)
        assertEquals(displayOptions, copied.displayOptions)
        assertEquals("Martínez López", copied.apellidos)

        assertEquals("Juan Martínez López", copied.legalName)
        assertEquals("Dr. Juan Martínez López, PhD", copied.formalName)
        assertEquals("Dr. Juan \"Jano\" Martínez López, PhD", copied.biographicalName)
        assertEquals("Jano Martínez López", copied.commonName)
        assertEquals("Jano Martínez López", copied.informalName)
        assertEquals("Martínez López, Juan", copied.indexedName)
        assertEquals("JML", copied.initials)
    }

    @Test
    fun `WHEN converting HispanicPersonalName to json representation and back to WesternPersonalName THEN fields are preserved and restored properly`() {
        val givenName = "Juan"
        val paternalName = "Martínez"
        val maternalName = "López"
        val honorific = "Dr."
        val nickname = "Jano"
        val suffixes = listOf("PhD")
        val displayOptions = setOf<DisplayOption>()

        val original = HispanicPersonalName(givenName, paternalName, maternalName, honorific, nickname, suffixes, displayOptions)
        val reconstructed = HispanicPersonalName(original.toJson())

        assertNotNull(reconstructed)

        assertEquals(honorific, reconstructed.honorific)
        assertEquals(givenName, reconstructed.givenName)
        assertEquals(nickname, reconstructed.nickname)
        assertEquals(paternalName, reconstructed.paternalName)
        assertEquals(maternalName, reconstructed.maternalName)
        assertEquals(suffixes, reconstructed.suffixes)
        assertEquals(displayOptions, reconstructed.displayOptions)
        assertEquals("Martínez López", reconstructed.apellidos)

        assertEquals("Juan Martínez López", reconstructed.legalName)
        assertEquals("Dr. Juan Martínez López, PhD", reconstructed.formalName)
        assertEquals("Dr. Juan \"Jano\" Martínez López, PhD", reconstructed.biographicalName)
        assertEquals("Jano Martínez López", reconstructed.commonName)
        assertEquals("Jano Martínez López", reconstructed.informalName)
        assertEquals("Martínez López, Juan", reconstructed.indexedName)
        assertEquals("JML", reconstructed.initials)
    }

    @Test
    fun `WHEN creating EasternPersonalName with all valid fields THEN construction succeeds and name forms are correct`() {
        val honorific = "Mrs."
        val familyName = "Gongsun"
        val generationName = "Yang"
        val givenName = "Bo"
        val westernName = "Sarah"
        val suffixes = listOf("Esq.")
        val displayOptions = setOf<DisplayOption>()
        val name = EasternPersonalName(familyName, generationName, givenName, westernName, honorific, null, suffixes, displayOptions)

        assertNotNull(name)

        assertEquals(honorific, name.honorific)
        assertEquals(familyName, name.familyName)
        assertEquals(generationName, name.generationName)
        assertEquals(givenName, name.givenName)
        assertEquals(westernName, name.westernName)
        assertEquals(suffixes, name.suffixes)
        assertEquals(displayOptions, name.displayOptions)

        assertEquals("Yangbo Gongsun", name.legalName)
        assertEquals("Mrs. Gongsun Yangbo, Esq.", name.formalName)
        assertEquals("Mrs. Sarah Gongsun Yangbo, Esq.", name.biographicalName)
        assertEquals("Gongsun Yangbo", name.commonName)
        assertEquals("Gongsun Yangbo", name.informalName)
        assertEquals("Gongsun, Yangbo", name.indexedName)
        assertEquals("GY", name.initials)
    }

    @Test
    fun `WHEN creating minimal EasternPersonalName THEN construction succeeds and name forms are correct`() {
        val name = EasternPersonalName("Gongsun", "Yang", "Bo")

        assertNotNull(name)

        assertEquals("Gongsun", name.familyName)
        assertEquals("Yang", name.generationName)
        assertEquals("Bo", name.givenName)
        assertEquals("Yangbo", name.fullGiven)

        assertEquals("Yangbo Gongsun", name.legalName)
        assertEquals("Gongsun Yangbo", name.formalName)
        assertEquals("Gongsun Yangbo", name.biographicalName)
        assertEquals("Gongsun Yangbo", name.commonName)
        assertEquals("Gongsun Yangbo", name.informalName)
        assertEquals("Gongsun, Yangbo", name.indexedName)
        assertEquals("GY", name.initials)
    }

    @Test
    fun `WHEN using the copy constructor on an EasternPersonalName THEN all fields are copied properly`() {
        val honorific = "Mrs."
        val familyName = "Gongsun"
        val generationName = "Yang"
        val givenName = "Bo"
        val westernName = "Sarah"
        val suffixes = listOf("Esq.")
        val displayOptions = setOf<DisplayOption>()

        val original = EasternPersonalName(familyName, generationName, givenName, westernName, honorific, null, suffixes, displayOptions)
        val copied = EasternPersonalName(original)

        assertNotNull(copied)

        assertEquals(honorific, copied.honorific)
        assertEquals(familyName, copied.familyName)
        assertEquals(generationName, copied.generationName)
        assertEquals(givenName, copied.givenName)
        assertEquals(westernName, copied.westernName)
        assertEquals(suffixes, copied.suffixes)
        assertEquals(displayOptions, copied.displayOptions)

        assertEquals("Yangbo Gongsun", copied.legalName)
        assertEquals("Mrs. Gongsun Yangbo, Esq.", copied.formalName)
        assertEquals("Mrs. Sarah Gongsun Yangbo, Esq.", copied.biographicalName)
        assertEquals("Gongsun Yangbo", copied.commonName)
        assertEquals("Gongsun Yangbo", copied.informalName)
        assertEquals("Gongsun, Yangbo", copied.indexedName)
        assertEquals("GY", copied.initials)
    }

    @Test
    fun `WHEN converting EasternPersonalName to json representation and back to WesternPersonalName THEN fields are preserved and restored properly`() {
        val honorific = "Mrs."
        val familyName = "Gongsun"
        val generationName = "Yang"
        val givenName = "Bo"
        val westernName = "Sarah"
        val suffixes = listOf("Esq.")
        val displayOptions = setOf<DisplayOption>()

        val original = EasternPersonalName(familyName, generationName, givenName, westernName, honorific, null, suffixes, displayOptions)
        val reconstructed = EasternPersonalName(original.toJson())

        assertNotNull(reconstructed)

        assertEquals(honorific, reconstructed.honorific)
        assertEquals(familyName, reconstructed.familyName)
        assertEquals(generationName, reconstructed.generationName)
        assertEquals(givenName, reconstructed.givenName)
        assertEquals(westernName, reconstructed.westernName)
        assertEquals(suffixes, reconstructed.suffixes)
        assertEquals(displayOptions, reconstructed.displayOptions)

        assertEquals("Yangbo Gongsun", reconstructed.legalName)
        assertEquals("Mrs. Gongsun Yangbo, Esq.", reconstructed.formalName)
        assertEquals("Mrs. Sarah Gongsun Yangbo, Esq.", reconstructed.biographicalName)
        assertEquals("Gongsun Yangbo", reconstructed.commonName)
        assertEquals("Gongsun Yangbo", reconstructed.informalName)
        assertEquals("Gongsun, Yangbo", reconstructed.indexedName)
        assertEquals("GY", reconstructed.initials)
    }
}
