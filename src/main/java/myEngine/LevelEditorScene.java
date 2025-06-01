package myEngine;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {


    private  String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}\n";

    private final String fragmentShaderSrc = "\n" +
            "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "\n" +
            "}";

    private int vertexShaderID, fragmentShaderID, shaderProgramID;

    private float[] vertexArray = {
            //position                  //color
            0.5f, -0.5f, 0.0f,          1.0f,0.0f,0.0f,1.0f, //Bottom right
            -0.5f, 0.5f, 0.0f,          0.0f,0.0f,0.0f,1.0f, //Top left
            0.5f, 0.5f, 0.0f,           0.0f,0.0f,1.0f,1.0f, //Top right
            -0.5f, -0.5f, 0.0f,         1.0f,1.0f,0.0f,1.0f //Bottom left
    };
    //IMPORTANT THIS MUST BE IN COUNTER-CLOCKWISE ORDER!
    private int[] elementArray = {
            0,2,1,
            0,1,3
    };
    private int vaoID, vboID, eboID;

    public LevelEditorScene() {
        init();
    }

    @Override
    public void update(float dt) {
        //Bind shader Program
        glUseProgram(shaderProgramID);
        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        //Enable the vertex Attributes pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(0);

        glBindVertexArray(0);
        glUseProgram(0);  //this should be a try with resources with an AutoCloseable... just for better readability, and so we do not acidentally forget to do it...



    }

    @Override
    public void init() {
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

        //Generate Buffer objects

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer of vertices;
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex Buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create the indices and upload;
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex Attributes Pointers

        int positionSize  = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeInBytes = (positionSize + colorSize) + floatSizeBytes;
        glVertexAttribPointer(0,positionSize,GL_FLOAT, false, vertexSizeInBytes,0);  //The first 0 corresponds to layout (location=0) in vec3 aPos;
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1,colorSize,GL_FLOAT, false, vertexSizeInBytes,positionSize * floatSizeBytes);  //The first 1 corresponds to layout (location=1) in vec4 aColor;
                                                                                                                                    // The 0 resp position Size in the last field is the offset (in Bytes)
        glEnableVertexAttribArray(1);





    }
}
