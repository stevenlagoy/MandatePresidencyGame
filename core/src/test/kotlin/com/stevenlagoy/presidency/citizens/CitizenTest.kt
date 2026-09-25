package com.stevenlagoy.presidency.citizens

import com.stevenlagoy.presidency.citizens.attributes.CharacterAppearance
import com.stevenlagoy.presidency.citizens.attributes.Family
import com.stevenlagoy.presidency.citizens.attributes.Sex
import com.stevenlagoy.presidency.citizens.attributes.names.WesternPersonalName
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.Manager
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CitizenTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            engine.init()
            assert(engine.state == Manager.ManagerState.ACTIVE) { "Failed to initialize engine" }
        }

        val engine: Engine = Engine()
    }

    @Test
    fun `WHEN creating Citizen with empty fields THEN construction succeeds`() {
        val citizen = Citizen(engine)
        assertNotNull(citizen)
        assertNotNull(citizen.sex)
        assertNotNull(citizen.birthday)
        assertNotNull(citizen.demographics)
        assertNotNull(citizen.family)
        assertNotNull(citizen.appearance)
        assertNotNull(citizen.name)
        assertNotNull(citizen.origin)
        assertNotNull(citizen.location)
        assertNotNull(citizen.residence)
        assertNull(citizen.financialProfile)
    }

    @Test
    fun `WHEN creating Citizen with all valid fields THEN construction succeeds and fields are correct`() {
        val sex = Sex.FEMALE
        val birthday = LocalDate.of(1970, 1, 1)
        val demographics = engine.DEMOGRAPHICS_MANAGER.commonDemographics
        val family = Family(engine, null, null, null, mutableSetOf())
        val appearance = CharacterAppearance()
        val name = WesternPersonalName()
    }



}
