package main.core.graphics;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;

import core.JSONObject;
import main.core.Main;
import main.core.Manager;
import main.core.utils.Logger;

public final class GraphicsManager extends Manager {

    // GRAPHICS CONSTANTS -------------------------------------------------------------------------

    public static final float FOV = (float) Math.toRadians(60.0f);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 10000.0f;
    public static final float SPECULAR_POWER = 10f;

    public static final float CAMERA_MOVE_SPEED = 0.01f;
    public static final float MOUSE_SENSITIVITY = 0.2f;

    public static final int MAX_SPOT_LIGHTS = 5;
    public static final int MAX_POINT_LIGHTS = 5;

    public static final String TITLE = "Engine Test";

    public static final Vector3f AMBIENT_LIGHT = new Vector3f(0.8f, 0.8f, 0.8f);
    public static final Vector4f DEFAULT_COLOR = new Vector4f(1, 1, 1, 1);

    public static final long NANOSECOND = 1_000_000_000;
    public static final float FRAMERATE = 1000;

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    public int fps;
    private float frametime = 1.0f / FRAMERATE;
    private boolean isRunning;
    private Window window;
    public  Window getWindow() { return window; }
    private GLFWErrorCallback errorCallback;
    private MouseInput mouse;
    private ILogic gameLogic;

    private ManagerState currentState;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    public GraphicsManager() {
        currentState = ManagerState.INACTIVE;
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    @Override
    public boolean init() {
        boolean successFlag = true;
        double startTime = Main.Engine().getProgramTime();
        Logger.log(String.format("%s starting at %f", this.getClass().getSimpleName(), startTime));
        currentState = successFlag ? ManagerState.ACTIVE : ManagerState.ERROR;
        double endTime = Main.Engine().getProgramTime(); 
        Logger.log(String.format("%s initialized %s at %f. Elapsed: %f", this.getClass().getSimpleName(), successFlag ? "successfully" : "unsuccessfully", endTime, endTime - startTime));
        return successFlag;
    }

    @Override
    public ManagerState getState() {
        return currentState;
    }

    @Override
    public boolean cleanup() {
        boolean successFlag = true;
        currentState = ManagerState.INACTIVE;
        if (!successFlag) currentState = ManagerState.ERROR;
        return successFlag;
    }

    // INSTANCE METHODS ---------------------------------------------------------------------------

    public void input() {
        gameLogic.input();
        mouse.input();
    }

    public void render() {
        gameLogic.render();
        window.update();
    }

    public void update(float interval) {
        gameLogic.update(interval, mouse);
    }

    public void loadTextures() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadTextures'");
    }

    /** Return the total {width, height} of the screen at layer units away from the camera. */
    public float[] calculateQuadDimensions(int layer) {
        float width, height;
        height = 10f;
        width = 20f;
        return new float[] { width, height };
    }

    public boolean containsPoint(float x, float y, float x1, float y1, float x2, float y2) {
        return (
            ( // X is in range
                (x > x1 && x < x2) ||
                (x > x2 && x < x1)
            ) && ( // Y is in range
                (y > y1 && y < y2) ||
                (y > y2 && y < y1)
            )
        );
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Manager fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject(this.getClass().getSimpleName());
    }

    @Override
    public Manager fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
    
}
