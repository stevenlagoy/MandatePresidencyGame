package com.stevenlagoy.presidency.characters.attributes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.stevenlagoy.jsonic.JSONObject;

public final class PersonalityTest {

    @Test
    public void testPersonalityToRepr() {
        PersonalityJava p = new PersonalityJava();
        String repr = p.toRepr();
        assertNotNull(repr);
    }

    @Test
    public void testPersonalityToJson() {
        PersonalityJava p = new PersonalityJava();
        JSONObject json = p.toJson();
        assertEquals("personality", json.getKey());
    }

}
