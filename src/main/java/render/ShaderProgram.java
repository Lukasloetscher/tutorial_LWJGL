package render;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.Closeable;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class ShaderProgram implements InterfaceShaderProgram{

    public static void main(String[] args){
        ShaderProgram test = new ShaderProgram("assets/shaders/defaults/vertex.glsl","assets/shaders/defaults/fragment.glsl");
    }

    private int shaderProgramID;
    private String vertexShaderSrc;
    private String fragmentShaderSrc;


    public ShaderProgram(String vertexShaderFilepath, String fragmentShaderFilepath){
        readFiles(vertexShaderFilepath,fragmentShaderFilepath);
        linkAndCompile();
    }

    private void readFiles(String vertexShaderFilepath, String fragmentShaderFilepath){
        try{
            vertexShaderSrc = new String(Files.readAllBytes(Paths.get(vertexShaderFilepath)));
            fragmentShaderSrc = new String(Files.readAllBytes(Paths.get(fragmentShaderFilepath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    private void linkAndCompile(){

        //TODO Move the shaders to their own classes
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
    @Override
    public ShaderAutoCloseable useProgram(){
        glUseProgram(shaderProgramID);
        return new ShaderAutoCloseable();
    }

    @Override
    public void uploadMat4f(String uniformVarName, Matrix4f matrixToUpload) {  //TODO make this able to specify Shader...
            int varLocation = glGetUniformLocation(shaderProgramID,uniformVarName);
            FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
            matrixToUpload.get(matBuffer);
            glUniformMatrix4fv(varLocation,false,matBuffer);

    }


}
