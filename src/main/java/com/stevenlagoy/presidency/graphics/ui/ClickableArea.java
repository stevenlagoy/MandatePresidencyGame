package com.stevenlagoy.presidency.graphics.ui;

import org.joml.Vector2d;

import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.graphics.MouseInput;

public class ClickableArea {
    private float x1, y1, x2, y2; // corners
    private boolean wasLeftClicked;
    private boolean wasRightClicked;
    private boolean isHover;
    private Runnable onLeftClick, onRightClick;

    private final Engine ENGINE;

    public ClickableArea(Engine engine, float x1, float y1, float x2, float y2, Runnable onLeftClick,
            Runnable onRightClick) {
        this.ENGINE = engine;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.onLeftClick = onLeftClick;
        this.onRightClick = onRightClick;
        this.wasLeftClicked = false;
        this.wasRightClicked = false;
        this.isHover = false;
    }

    public boolean update(MouseInput mouse) {
        Vector2d pos = mouse.getCurrentPosition();

        float ndcX = (2.0f * (float) pos.x / ENGINE.GraphicsManager().getWindow().getWidth()) - 1.0f;
        float ndcY = 1.0f - (2.0f * (float) pos.y / ENGINE.GraphicsManager().getWindow().getHeight());

        System.out.printf("Screen: (%.2f, %.2f) -> NDC: (%.2f, %.2f)%n",
                pos.x, pos.y, ndcX, ndcY);

        if (containsPoint(ndcX, ndcY)) {
            isHover = true;
            if (mouse.isLeftButtonPress()) {
                wasLeftClicked = true;
                if (onLeftClick != null)
                    onLeftClick.run();
            }
            if (mouse.isRightButtonPress()) {
                wasRightClicked = true;
                if (onRightClick != null)
                    onRightClick.run();
            }
            return true;
        } else
            isHover = false;
        return false;
    }

    public boolean wasLeftClicked() {
        return wasLeftClicked;
    }

    public boolean wasRightClicked() {
        return wasRightClicked;
    }

    public boolean isHover() {
        return isHover;
    }

    private boolean containsPoint(float x, float y) {
        return ENGINE.GraphicsManager().containsPoint(x, y, x1, y1, x2, y2);
    }
}
