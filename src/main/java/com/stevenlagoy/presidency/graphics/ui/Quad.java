package com.stevenlagoy.presidency.graphics.ui;

import org.joml.Vector3f;
import com.stevenlagoy.presidency.graphics.entity.Entity;
import com.stevenlagoy.presidency.graphics.entity.QuadModel;

public class Quad extends Entity {

    public Quad(QuadModel model, Vector3f pos, Vector3f rotation, float scale) {
        super(model, pos, rotation, scale);
    }

    public Quad(QuadModel model, Vector3f pos) {
        super(model, pos);
    }
}
