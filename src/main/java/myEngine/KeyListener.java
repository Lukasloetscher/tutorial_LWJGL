package myEngine;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static final KeyListener instance = new KeyListener();
    private final boolean[] keyPressed = new boolean[350]; //Look up this 350...

    private KeyListener(){

    }
    public static KeyListener get(){
        return instance;
    }
    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action == GLFW_PRESS){
            get().keyPressed[key] = true;   //I think we do not need an error handling here. handling the error for when an int larger than 350 is sent would be teh same error anyway as the one thrown by the Array  natively,
        }else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode){
        return get().keyPressed[keyCode];
    }

    public static void debugPrintKeys(){
        System.out.println(Arrays.toString(get().keyPressed));
    }
}
