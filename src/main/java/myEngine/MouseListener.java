package myEngine;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class MouseListener {
    private static final MouseListener instance = new MouseListener();
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private final boolean  mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        scrollX = 0.0;
        scrollY = 0.0;
        xPos = 0.0;
        yPos = 0.0;
        lastX = 0.0;
        lastY = 0.0;

    }

    public static MouseListener get() {
        return instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = IntStream.range(0, get().mouseButtonPressed.length).mapToObj(i -> get().mouseButtonPressed[i]).anyMatch(b -> b); //Checks if at least one mouse button is clicked.
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button >= get().mouseButtonPressed.length) {
            System.out.println("Error pressed mouse button without binding");
            return;
        }
        if (action == GLFW_PRESS) {
            get().mouseButtonPressed[button] = true;
        } else {
            get().mouseButtonPressed[button] = false;
            get().isDragging = false;
        }

    }
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float getDx() {
        return (float)(get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float)(get().scrollX);
    }

    public static float getScrollY() {
        return (float)(get().scrollY);
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if (button >= get().mouseButtonPressed.length) {
            throw new IllegalArgumentException("The requested MouseButton " + button + " does not exist.");
        }

        return get().mouseButtonPressed[button];
    }




}
