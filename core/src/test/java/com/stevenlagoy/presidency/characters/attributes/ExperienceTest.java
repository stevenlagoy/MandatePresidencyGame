package com.stevenlagoy.presidency.characters.attributes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public final class ExperienceTest {
    
    @Test
    public void testExperienceToRepr() {
        Experience e = new Experience();
        String repr = e.toRepr();
        assertNotNull(repr);
    }

}