package com.stevenlagoy.presidency.characters.attributes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.stevenlagoy.jsonic.JSONObject;

public final class PersonalityTest {
    
    @Test
    public void testPersonalityToRepr() {
        Personality p = new Personality();
        String repr = p.toRepr();
        assertNotNull(repr);
    }

    @Test
    public void testPersonalityToJson() {
        Personality p = new Personality();
        JSONObject json = p.toJson();
        assertEquals("personality", json.getKey());
    }

}