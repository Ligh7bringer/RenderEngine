package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

/**
 * Created by Svetlozar Georgiev on 01/05/2017.
 */

public class DisplayManager {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 768;
    private static final int FPS_CAP = 120;

    private static long lastFrameTime;
    private static float deltaTime;

    public static void createDisplay() {

        ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat().withDepthBits(24), attribs);
            Display.setTitle("Render Engine");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        deltaTime = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getDeltaTime() {
        return deltaTime;
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    private static long getCurrentTime(){
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }
}
