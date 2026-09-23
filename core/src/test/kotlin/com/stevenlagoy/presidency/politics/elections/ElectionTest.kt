package com.stevenlagoy.presidency.politics.elections

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.Manager
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ElectionTest {

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
    fun `WHEN creating empty Election THEN construction succeeds`() {
        val election = Election(engine)
        assertNotNull(election)
        assertNotNull(election.targetPosition)
        assert(election.campaigns.isEmpty())
    }

    /*
    First Past the Post test cases:
    Candidate A - 1000
    Candidate B - 0

    Candidate A - 0
    Candidate B - 1000

    Candidate A - 1000
    Candidate B - 0
    Candidate C - 0
    Candidate D - 0

    Candidate A - 0
    Candidate B - 0
    Candidate C - 0
    Candidate D - 1000

    Test with all permutations of one to ten candidates.

    Candidate A - 750
    Candidate B - 250

    Candidate A - 250
    Candidate B - 750

    Candidate A - 501
    Candidate B - 500

    Candidate A - 499
    Candidate B - 500

    Candidate A - 500
    Candidate B - 500

    Candidate A - 10_000_000_001
    Candidate B - 10_000_000_000

    Candidate A - 0
    Candidate B - 0

    Candidate A - -1
    Candidate B - 0

    Candidate A = 0

    Candidate A = 1000

    Also try a lot of random numbers from 0 to 1000 for each candidate from one to ten candidates.

     */

}
