package com.stevenlagoy.presidency.graphics.ui;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;

import com.stevenlagoy.presidency.app.Main;
import com.stevenlagoy.presidency.graphics.Camera;
import com.stevenlagoy.presidency.graphics.MouseInput;
import com.stevenlagoy.presidency.graphics.Transformation;
import com.stevenlagoy.presidency.graphics.entity.Entity;
import com.stevenlagoy.presidency.graphics.entity.Texture;
import com.stevenlagoy.presidency.graphics.entity.TextureManager;

public class Button extends Entity {

    private Entity entity;

    private Texture regularTexture;
    private Texture hoverTexture;
    private Texture leftClickTexture;
    private Texture rightClickTexture;

    private Runnable onHover;
    private Runnable onUnHover;
    private Runnable onLeftClick;
    private Runnable onLeftRelease;
    private Runnable onRightClick;
    private Runnable onRightRelease;

    private boolean isLeftClick;
    private boolean isRightClick;
    private boolean isHover;
    private boolean wasHover;
    private boolean wasLeftClick;
    private boolean wasRightClick;

    public Button(Entity entity, Texture regularTexture, Texture hoverTexture, Runnable onHover, Runnable onUnHover,
            Texture leftClickTexture, Runnable onLeftClick, Runnable onLeftRelease, Texture rightClickTexture,
            Runnable onRightClick, Runnable onRightRelease) {
        super(entity.getModel(), entity.getPos(), entity.getRotation(), entity.getScale());
        this.entity = entity;
        this.regularTexture = regularTexture;
        this.hoverTexture = hoverTexture;
        this.onHover = onHover;
        this.onUnHover = onUnHover;
        this.leftClickTexture = leftClickTexture;
        this.onLeftClick = onLeftClick;
        this.onLeftRelease = onLeftRelease;
        this.rightClickTexture = rightClickTexture;
        this.onRightClick = onRightClick;
        this.onRightRelease = onRightRelease;

        this.isLeftClick = false;
        this.isRightClick = false;
        this.isHover = false;
        this.wasHover = false;
        this.wasLeftClick = false;
        this.wasRightClick = false;
    }

    public Button(Entity entity, String regularTextureName, String hoverTextureName, Runnable onHover,
            Runnable onUnHover, String leftClickTextureName, Runnable onLeftClick, Runnable onLeftRelease,
            String rightClickTextureName, Runnable onRightClick, Runnable onRightRelease) {
        this(
                entity,
                TextureManager.getTexture(regularTextureName),
                TextureManager.getTexture(hoverTextureName),
                onHover,
                onUnHover,
                TextureManager.getTexture(leftClickTextureName),
                onLeftClick,
                onLeftRelease,
                TextureManager.getTexture(rightClickTextureName),
                onRightClick,
                onRightRelease);
    }

    // TODO: this needs to be able to un onUnHover, onLeftRelease, onRightRelease

    public void update(MouseInput mouse, Camera camera) {
        Vector2d mousePos = mouse.getCurrentPosition();

        float ndcX = (float) ((2.0 * mousePos.x) / Main.Engine().GraphicsManager().getWindow().getWidth() - 1.0);
        float ndcY = (float) (1.0 - (2.0 * mousePos.y) / Main.Engine().GraphicsManager().getWindow().getHeight());
        float ndcZ = -1.0f; // near plane

        Matrix4f viewMatrix = Transformation.getViewMatrix(camera);

        Vector3f rayOrigin = new Vector3f(camera.getPosition());
        Vector3f rayDirection = Main.Engine().GraphicsManager().getWindow().calculateRayDirection(ndcX, ndcY,
                viewMatrix);

        isHover = entity.intersects(rayOrigin, rayDirection);

        isLeftClick = isHover && mouse.isLeftButtonPress();
        isRightClick = isHover && mouse.isRightButtonPress();

        if (isLeftClick) {
            if (onLeftClick != null)
                onLeftClick.run();
            if (leftClickTexture != null)
                entity.getModel().setTexture(leftClickTexture);
        } else if (isRightClick) {
            if (onRightClick != null)
                onRightClick.run();
            if (rightClickTexture != null)
                entity.getModel().setTexture(rightClickTexture);
        } else if (isHover) {
            if (onHover != null)
                onHover.run();
            if (hoverTexture != null)
                entity.getModel().setTexture(hoverTexture);
        } else {
            entity.getModel().setTexture(regularTexture);
        }

        if (wasHover) {

        } else if (wasLeftClick) {

        } else if (wasRightClick) {

        }
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isLeftClicked() {
        return isLeftClick;
    }

    public boolean isRightClicked() {
        return isRightClick;
    }

    public boolean isHover() {
        return isHover;
    }

    public void runOnHover() {
        if (onHover != null)
            onHover.run();
    }

    public void runOnUnHover() {
        if (onUnHover != null)
            onUnHover.run();
    }

    public void runOnLeftClick() {
        if (onLeftClick != null)
            onLeftClick.run();
    }

    public void runOnLeftRelease() {
        if (onLeftRelease != null)
            onLeftRelease.run();
    }

    public void runOnRightClick() {
        if (onRightClick != null)
            onRightClick.run();
    }

    public void runOnRightRelease() {
        if (onRightRelease != null)
            onRightRelease.run();
    }

}
