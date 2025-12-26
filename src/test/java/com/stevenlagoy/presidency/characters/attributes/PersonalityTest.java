package com.stevenlagoy.presidency.characters.attributes;

import org.junit.*;

import core.JSONObject;

import static org.junit.Assert.*;

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
