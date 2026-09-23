package com.stevenlagoy.presidency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class MapCameraController extends InputAdapter {

    private static final float MIN_DISTANCE = 10f;
    private static final float MAX_DISTANCE = 4000f;
    private static final float MIN_PITCH = 15f;
    private static final float MAX_PITCH = 85f;

    private static final float PAN_SPEED = 0.0015f;
    private static final float ROTATE_SPEED = 0.2f;
    private static final float ZOOM_SPEED = 15f;

    private final PerspectiveCamera camera;

    private final Vector3 target = new Vector3();
    private float azimuthDeg;
    private float pitchDeg;
    private float distance;

    private final Vector3 initialTarget = new Vector3();
    private final float initialAzimuth ;
    private final float initialPitch;
    private final float initialDistance;

    private int lastX, lastY;
    private boolean panning, rotating;

    public MapCameraController(
        PerspectiveCamera camera, Vector3 startTarget,
        float startAzimuthDeg, float startPitchDeg, float startDistance
    ) {
        this.camera = camera;
        this.initialTarget.set(startTarget);
        this.initialAzimuth = startAzimuthDeg;
        this.initialPitch = startPitchDeg;
        this.initialDistance = startDistance;
        reset();
    }

    public void reset() {
        target.set(initialTarget);
        azimuthDeg = initialAzimuth;
        pitchDeg = initialPitch;
        distance = initialDistance;
        updateCamera();
    }

    private void updateCamera() {
        float azimuthRad = azimuthDeg * MathUtils.degreesToRadians;
        float pitchRad = pitchDeg * MathUtils.degreesToRadians;
        float horizontalDist = distance * MathUtils.cos(pitchRad);

        camera.position.set(
            target.x + horizontalDist * MathUtils.sin(azimuthRad),
            target.y + distance * MathUtils.sin(pitchRad),
            target.z + horizontalDist * MathUtils.cos(azimuthRad)
        );
        camera.up.set(Vector3.Y);
        camera.lookAt(target);
        camera.update();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastX = screenX;
        lastY = screenY;
        return switch (button) {
            case Input.Buttons.RIGHT -> {
                panning = true;
                yield true;
            }
            case Input.Buttons.MIDDLE -> {
                rotating = true;
                yield true;
            }
            default -> false;
        };
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            panning = false;
        }
        if (button == Input.Buttons.MIDDLE) {
            rotating = false;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return switch (keycode) {
            case Input.Keys.ESCAPE -> {
                Gdx.app.exit();
                yield true;
            }
            case Input.Keys.UP, Input.Keys.DOWN -> {
                panning = true;
                yield true;
            }
            default -> false;
        };
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP) {
            panning = false;
        }
        if (keycode == Input.Keys.DOWN) {
            panning = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        int dx = screenX - lastX;
        int dy = screenY - lastY;
        lastX = screenX;
        lastY = screenY;
        boolean result = false;

        if (panning) {
            pan(dx, -dy);
            result = true;
        }
        if (rotating) {
            rotate(dx, dy);
            result = true;
        }
        return result;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        distance = MathUtils.clamp(distance + amountY * ZOOM_SPEED, MIN_DISTANCE, MAX_DISTANCE);
        updateCamera();
        return true;
    }

    private void pan(int dx, int dy) {
        float azimuthRad = azimuthDeg * MathUtils.degreesToRadians;
        float scale = distance * PAN_SPEED;

        float rightX = MathUtils.cos(azimuthRad);
        float rightZ = -MathUtils.sin(azimuthRad);
        float forwardX = MathUtils.sin(azimuthRad);
        float forwardZ = MathUtils.cos(azimuthRad);

        target.x += (-dx * rightX + dy * forwardX) * scale;
        target.z += (-dx * rightZ + dy * forwardZ) * scale;
        updateCamera();
    }

    private void rotate(int dx, int dy) {
        azimuthDeg -= dx * ROTATE_SPEED;
        pitchDeg = MathUtils.clamp(pitchDeg + dy * ROTATE_SPEED, MIN_PITCH, MAX_PITCH);
        updateCamera();
    }
}
