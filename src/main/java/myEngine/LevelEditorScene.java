package myEngine;

import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene {


    private final String vertexShaderSrc = "#version 330 core\n" +
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



    public LevelEditorScene() {

    }

    @Override
    public void update(float dt) {

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


    }
}
