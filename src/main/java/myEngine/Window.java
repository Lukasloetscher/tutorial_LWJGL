package myEngine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;


public class Window {
    private static final Window myWindow = new Window();
    private int width,height;
    private String title;

    private long glfwWindow;

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
        glfwWindow = glfwCreateWindow(width, height, title, 0, 0);






    }

}
