package com.stevenlagoy.presidency.characters.attributes;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.stevenlagoy.jsonic.JSONObject;

public final class CharacterModelTest {
    
    @Test
    public void testCharacterModelConstructors() {
        CharacterModel cm1 = new CharacterModel();
        assertEquals(CharacterModel.DEFAULT_AGE, cm1.getVisualAge());
        CharacterModel cm2 = new CharacterModel(cm1);
        assertEquals(cm1.getVisualAge(), cm2.getVisualAge());
        CharacterModel cm3 = new CharacterModel(30);
        assertEquals(30, cm3.getVisualAge());
    }

    @Test
    public void testVisualAge() {
        CharacterModel cm = new CharacterModel();
        cm.setVisualAge(30);
        assertEquals(30, cm.getVisualAge());
    }

    @Test
    public void testCharacterModelToRepr() {
        CharacterModel cm = new CharacterModel();
        int visualAge = cm.getVisualAge();
        String repr = cm.toRepr();
        assertTrue(repr.contains(String.format("visualAge:%d;", visualAge)));
    }

    @Test
    public void testCharacterModelToJson() {
        CharacterModel cm = new CharacterModel();
        int visualAge = cm.getVisualAge();
        JSONObject json = cm.toJson();
        assertEquals("character_model", json.getKey());
        assertEquals(visualAge, ((JSONObject) json.getValue()).getAsNumber());
    }

    @Test
    public void testCharacterModelFromJson() {
        CharacterModel cm = new CharacterModel(65);
        int visualAge = cm.getVisualAge();
        JSONObject json = cm.toJson();
        cm = cm.fromJson(json);
        assertEquals(visualAge, cm.getVisualAge());
    }

    @Test
    public void testCharacterModelFromJsonInvalid() {
        CharacterModel cm = new CharacterModel();
        JSONObject json = null;
        CharacterModel res = cm.fromJson(json);
        assertNull(res);
        json = new JSONObject("Not a character model");
        res = cm.fromJson(json);
        assertNull(res);
    }

}