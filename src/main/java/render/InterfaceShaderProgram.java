package render;

import org.joml.Matrix4f;

import java.io.Closeable;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.glUseProgram;

public interface InterfaceShaderProgram {

    public ShaderAutoCloseable useProgram();

    public class ShaderAutoCloseable implements Closeable {
        @Override
        public void close() throws IOException {
            glUseProgram(0);
        }
    }

    public void uploadMat4f(String uniformVarName, Matrix4f matrixToUpload);

}
