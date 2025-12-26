package com.stevenlagoy.presidency.characters.attributes;

import org.junit.*;
import static org.junit.Assert.*;

public final class ExperienceTest {
    
    @Test
    public void testExperienceToRepr() {
        Experience e = new Experience();
        String repr = e.toRepr();
        assertNotNull(repr);
    }

}
