package render;

import org.lwjgl.BufferUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Shader {

    public static void main(String[] args){
        Shader test = new Shader("assets/shaders/defaults/vertex.glsl","assets/shaders/defaults/fragment.glsl");
    }

    private int shaderProgramID;
    private String vertexShaderSrc;
    private String fragmentShaderSrc;


    public Shader(String vertexShaderFilepath, String fragmentShaderFilepath){
        readFiles(vertexShaderFilepath,fragmentShaderFilepath);
        linkAndCompile();
    }

    public void readFiles(String vertexShaderFilepath, String fragmentShaderFilepath){
        try{
            vertexShaderSrc = new String(Files.readAllBytes(Paths.get(vertexShaderFilepath)));
            fragmentShaderSrc = new String(Files.readAllBytes(Paths.get(fragmentShaderFilepath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    public void linkAndCompile(){
        int vertexShaderID,fragmentShaderID;

        //First load and compile vertex shader
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);

        //pass the shader source code to GPU
        glShaderSource(vertexShaderID, vertexShaderSrc);

        glCompileShader(vertexShaderID);

        //Check for errors in compilation

        int success = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexShaderID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'default Shader.glsl'");
            System.out.println("vertexShader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexShaderID, len));
            throw new RuntimeException();
        }


        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);

        //pass the shader source code to GPU
        glShaderSource(fragmentShaderID, fragmentShaderSrc);

        glCompileShader(fragmentShaderID);

        //Check for errors in compilation

        success = glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexShaderID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'default Shader.glsl'");
            System.out.println("fragmentShader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexShaderID, len));
            throw new RuntimeException();
        }

        //Link Shader and check for error
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexShaderID);
        glAttachShader(shaderProgramID, fragmentShaderID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'Linking '");
            System.out.println("linking Program failed");
            System.out.println(glGetProgramInfoLog(vertexShaderID, len));
            throw new RuntimeException();
        }


    }

    public void useShader(){
        glUseProgram(shaderProgramID);
    }

    public void detach(){
        glUseProgram(0);
    }



}
