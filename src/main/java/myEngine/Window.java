package myEngine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import util.Time;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private static final Window myWindow = new Window();
    private int width,height;
    private String title;

    private long glfwWindow;

    private static Scene currentScene = null;

    public static void changeScene(Scene newScene){
        currentScene = newScene;
    }


    private Window(){
        width = 1920;
        height = 1080;
        title = "Hello World";

    }
    public static Window get(){
        return myWindow;
    }

    public void run(){
        System.out.println("Hello World" + Version.getVersion());
        init();
        loop();

        //Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public void init(){
        //Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        //configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //create the Window
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL); //NULL ?
        if (glfwWindow == NULL){
            throw new RuntimeException("Failed to create the GLFW window");
        }
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(glfwWindow, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });
        //Create Callbacks:
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallback);


        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        changeScene(new LevelEditorScene());

    }
    public void loop(){



        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        float frameBeginTime = Time.getTime();
        float frameEndTime;
        float dt = 0;
        while ( !glfwWindowShouldClose(glfwWindow) ) {
            // Set the clear color
            glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
            //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT); // clear the framebuffer

            currentScene.update(dt);

            glfwSwapBuffers(glfwWindow); // swap the color buffers

           

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            frameEndTime = Time.getTime();
             dt = frameEndTime - frameBeginTime;
            frameBeginTime = frameEndTime;



        }
    }


}
