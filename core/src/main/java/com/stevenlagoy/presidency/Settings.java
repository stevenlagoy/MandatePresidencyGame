package com.stevenlagoy.presidency;

import com.badlogic.gdx.Input.Keys;

public final class Settings {
    
    /** Default starting pitch */
    public static final float CAMERA_START_PITCH = 20f;
    /** Min pitch */
    public static final float CAMERA_MIN_PITCH = CAMERA_START_PITCH - 20f;
    /** Max pitch */
    public static final float CAMERA_MAX_PITCH = CAMERA_START_PITCH + 20f;
    /** Speed of camera pitch from mouse */
    public static final float CAMERA_PITCH_FACTOR = 0.3f;
    /** Our zoom multiplier (speed) */
    public static final float CAMERA_ZOOM_LEVEL_FACTOR = 0.5f;
    /** Rotation around player speed */
    public static final float CAMERA_ANGLE_AROUND_PLAYER_FACTOR = 0.2f;
    /** Min zoom distance */
    public static final float CAMERA_MIN_DISTANCE_FROM_PLAYER = 4;
    /** Max zoom distance */
    public static final float CAMERA_MAX_DISTANCE_FROM_PLAYER = 40;
    /** Factor of speed up when the fast/sprint key is held */
    public static final float SPEED_UP_FACTOR = 2.5f;

    public static final int MOVE_FORWARD_KEY       = Keys.W;
    public static final int MOVE_LEFT_KEY          = Keys.A;
    public static final int MOVE_BACKWARD_KEY      = Keys.S;
    public static final int MOVE_RIGHT_KEY         = Keys.D;
    public static final int SPEED_UP_KEY           = Keys.SHIFT_LEFT;
    public static final int CHANGE_CAMERA_MODE_KEY = Keys.TAB;
    public static final int CONTEXT_BACK_KEY       = Keys.ESCAPE;

    public static final int FOV                    = 67;
}
