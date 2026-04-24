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

//    @Test
//    fun `primary constructor`() {
//        val name = PersonalName(
//            NameForm.WESTERN,
//            "John",
//            "Michael",
//            "Doe",
//            "Smith",
//            "Alvarez",
//            "Jimenez",
//            "Johnnie",
//            "Paul",
//            "Peter",
//            "Mr.",
//            "Sr.",
//            listOf("PhD", "MD"),
//            setOf(DisplayOption.INCLUDE_MIDDLE, DisplayOption.ABBREVIATE_FIRST)
//        )
//        assertEquals(NameJava.NameForm.WESTERN, name.nameForm)
//        assertEquals("John", name.givenName)
//        assertEquals("Michael", name.middleName)
//        assertEquals("Doe", name.familyName)
//        assertEquals("Smith", name.birthSurname)
//        assertEquals("Alvarez", name.paternalName)
//        assertEquals("Jimenez", name.maternalName)
//        assertEquals("Johnnie", name.nickname)
//        assertEquals("Paul", name.religiousName)
//        assertEquals("Peter", name.westernName)
//        assertEquals("Mr.", name.honorific)
//        assertEquals("Sr.", name.ordinal)
//        assertEquals(listOf("PhD", "MD"), name.suffixes)
//        assertEquals(setOf(DisplayOption.INCLUDE_MIDDLE, DisplayOption.ABBREVIATE_FIRST), name.displayOptions)
//    }
//
//    @Test
//    fun `no-arg constructor`() {
//        val name = PersonalName()
//        assertEquals(NameForm.WESTERN, name.nameForm)
//        assertEquals("", name.givenName)
//        assertEquals("", name.middleName)
//        assertEquals("", name.familyName)
//        assertEquals("", name.birthSurname)
//        assertEquals("", name.paternalName)
//        assertEquals("", name.maternalName)
//        assertEquals("", name.nickname)
//        assertEquals("", name.religiousName)
//        assertEquals("", name.westernName)
//        assertEquals("", name.honorific)
//        assertEquals("", name.ordinal)
//        assertEquals(listOf<String>(), name.suffixes)
//        assertEquals(setOf<DisplayOption>(), name.displayOptions)
//    }
//
//    @Test
//    fun `copy constructor`() {
//        val originalName = PersonalName(
//            NameForm.WESTERN,
//            "John",
//            "Michael",
//            "Doe",
//            "Smith",
//            "Alvarez",
//            "Jimenez",
//            "Johnnie",
//            "Paul",
//            "Peter",
//            "Mr.",
//            "Sr.",
//            listOf("PhD", "MD"),
//            setOf(DisplayOption.INCLUDE_MIDDLE, DisplayOption.ABBREVIATE_FIRST)
//        )
//        val newName = PersonalName(originalName)
//        assertEquals(NameForm.WESTERN, newName.nameForm)
//        assertEquals("John", newName.givenName)
//        assertEquals("Michael", newName.middleName)
//        assertEquals("Doe", newName.familyName)
//        assertEquals("Smith", newName.birthSurname)
//        assertEquals("Alvarez", newName.paternalName)
//        assertEquals("Jimenez", newName.maternalName)
//        assertEquals("Johnnie", newName.nickname)
//        assertEquals("Paul", newName.religiousName)
//        assertEquals("Peter", newName.westernName)
//        assertEquals("Mr.", newName.honorific)
//        assertEquals("Sr.", newName.ordinal)
//        assertEquals(listOf("PhD", "MD"), newName.suffixes)
//        assertEquals(setOf(DisplayOption.INCLUDE_MIDDLE, DisplayOption.ABBREVIATE_FIRST), newName.displayOptions)
//    }
//
//    @Test
//    fun `secondary constructor -- givenName, middleName, lastName`() {
//        val name = PersonalName("John", "Michael", "Doe")
//        assertEquals(NameForm.WESTERN, name.nameForm)
//        assertEquals("John", name.givenName)
//        assertEquals("Michael", name.middleName)
//        assertEquals("Doe", name.familyName)
//        assertEquals("", name.birthSurname)
//        assertEquals("", name.paternalName)
//        assertEquals("", name.maternalName)
//        assertEquals("", name.nickname)
//        assertEquals("", name.religiousName)
//        assertEquals("", name.westernName)
//        assertEquals("", name.honorific)
//        assertEquals("", name.ordinal)
//        assertEquals(listOf<String>(), name.suffixes)
//        assertEquals(setOf<DisplayOption>(), name.displayOptions)
//    }
//
//    @Test
//    fun `property accessors and modifiers`() {
//        val name = PersonalName()
//        name.givenName = "John"
//        name.middleName = "Michael"
//        name.familyName = "Doe"
//        name.birthSurname = "Smith"
//        name.paternalName = "Alvarez"
//        name.maternalName = "Jimenez"
//        name.nickname = "Johnnie"
//        name.religiousName = "Paul"
//        name.westernName = "Peter"
//        name.honorific = "Mr."
//        name.ordinal = "Sr."
//        name.suffixes = listOf("PhD", "MD")
//        name.displayOptions = setOf(DisplayOption.INCLUDE_MIDDLE, DisplayOption.ABBREVIATE_FIRST)
//
//        assertEquals("John", name.givenName)
//        assertEquals("Michael", name.middleName)
//        assertEquals("Doe", name.familyName)
//        assertEquals("Smith", name.birthSurname)
//        assertEquals("Alvarez", name.paternalName)
//        assertEquals("Jimenez", name.maternalName)
//        assertEquals("Johnnie", name.nickname)
//        assertEquals("Paul", name.religiousName)
//        assertEquals("Peter", name.westernName)
//        assertEquals("Mr.", name.honorific)
//        assertEquals("Sr.", name.ordinal)
//        assertEquals(listOf("PhD", "MD"), name.suffixes)
//        assertEquals(setOf(DisplayOption.INCLUDE_MIDDLE, DisplayOption.ABBREVIATE_FIRST), name.displayOptions)
//
//        assertEquals("J", name.preferredFirst)
//        assertEquals("Michael", name.preferredMiddle)
//        name.displayOptions += DisplayOption.ABBREVIATE_MIDDLE
//        assertEquals("M", name.preferredMiddle)
//        name.displayOptions -= DisplayOption.ABBREVIATE_MIDDLE
//        assertEquals("J", name.preferredName)
//        name.displayOptions += DisplayOption.PREFER_MIDDLE
//        assertEquals("Michael", name.preferredName)
//        name.displayOptions += DisplayOption.INCLUDE_NICKNAME
//        assertEquals("Johnnie", name.preferredName)
//        assertEquals("PhD, MD", name.formattedSuffixes)
//    }
//
//    @Test
//    fun `hispanic nameform`() {
//        val name = PersonalName(NameForm.HISPANIC, "Alberto", "Miguel", "Ramos de la Cruz Salamanca")
//        name.nickname = "Beto"
//        name.displayOptions += DisplayOption.INCLUDE_NICKNAME
//        assertEquals(NameForm.HISPANIC, name.nameForm)
//        assertEquals("Alberto", name.givenName)
//        assertEquals("Miguel", name.middleName)
//        assertEquals("Ramos de la Cruz Salamanca", name.familyName)
//        assertEquals("", name.birthSurname)
//        assertEquals("Ramos de la Cruz", name.paternalName)
//        assertEquals("Salamanca", name.maternalName)
//        assertEquals("Beto", name.nickname)
//        assertEquals("", name.religiousName)
//        assertEquals("", name.westernName)
//        assertEquals("", name.honorific)
//        assertEquals("", name.ordinal)
//        assertEquals(listOf<String>(), name.suffixes)
//        assertEquals(setOf(DisplayOption.INCLUDE_NICKNAME), name.displayOptions)
//
//        assertEquals("Alberto Miguel Ramos de la Cruz Salamanca", name.legalName)
//        assertEquals("Alberto Miguel Ramos de la Cruz Salamanca", name.formalName)
//        assertEquals("Alberto Miguel \"Beto\" Ramos de la Cruz Salamanca", name.biographicalName)
//        assertEquals("Beto Ramos de la Cruz Salamanca", name.commonName)
//        assertEquals("Beto Ramos de la Cruz", name.informalName)
//
//        name.familyName = "Hernandez"
//        assertEquals("Hernandez", name.familyName)
//        assertEquals("Hernandez", name.paternalName)
//        assertEquals("", name.maternalName)
//
//        name.displayOptions += DisplayOption.MATERNAL_FIRST
//        name.familyName = "Lopez"
//        assertEquals("Lopez", name.familyName)
//        assertEquals("Lopez", name.maternalName)
//        assertEquals("", name.paternalName)
//
//        name.familyName = ""
//        assertEquals("", name.familyName)
//        assertEquals("", name.paternalName)
//        assertEquals("", name.maternalName)
//    }
//
//    @Test
//    fun `eastern nameform`() {
//        val name = PersonalName(NameForm.EASTERN, "Cheng", "Bo", "Heng")
//        assertEquals(NameForm.EASTERN, name.nameForm)
//        assertEquals("cheng", name.givenName)
//        assertEquals("Bocheng", name.givenNames)
//        assertEquals("Bo", name.middleName)
//        assertEquals("Heng", name.familyName)
//        assertEquals("Bocheng", name.preferredName)
//        name.displayOptions += DisplayOption.LATENT_GENERATION
//        assertEquals("Cheng", name.preferredName)
//        name.displayOptions -= DisplayOption.LATENT_GENERATION
//        name.westernName = "Peter"
//        assertEquals("Peter", name.westernName)
//
//        assertEquals("Bocheng Heng", name.legalName)
//        assertEquals("Heng Bocheng", name.formalName)
//        assertEquals("\"Peter\" Heng Bocheng", name.biographicalName)
//        assertEquals("Heng Bocheng", name.commonName)
//        assertEquals("Heng Bocheng", name.informalName)
//
//        name.displayOptions += DisplayOption.LATENT_GENERATION
//        assertEquals("Bocheng Heng", name.legalName)
//        assertEquals("Heng Bocheng", name.formalName)
//        assertEquals("\"Peter\" Heng Bocheng", name.biographicalName)
//        assertEquals("Cheng Heng", name.commonName)
//        assertEquals("Cheng Heng", name.informalName)
//
//        name.displayOptions += DisplayOption.INCLUDE_WESTERN
//        assertEquals("Bocheng Heng", name.legalName)
//        assertEquals("Heng Bocheng", name.formalName)
//        assertEquals("\"Peter\" Heng Bocheng", name.biographicalName)
//        assertEquals("Peter Heng", name.commonName)
//        assertEquals("Peter Heng", name.informalName)
//
//        name.displayOptions += DisplayOption.WESTERN_FIRST
//        assertEquals("Bocheng Heng", name.legalName)
//        assertEquals("Heng Bocheng", name.formalName)
//        assertEquals("\"Peter\" Heng Bocheng", name.biographicalName)
//        assertEquals("Peter Cheng Heng", name.commonName)
//        assertEquals("Peter Cheng", name.informalName)
//
//        name.displayOptions -= DisplayOption.LATENT_GENERATION
//        assertEquals("Bocheng Heng", name.legalName)
//        assertEquals("Heng Bocheng", name.formalName)
//        assertEquals("\"Peter\" Heng Bocheng", name.biographicalName)
//        assertEquals("Peter Bocheng Heng", name.commonName)
//        assertEquals("Peter Cheng", name.informalName)
//    }

}
