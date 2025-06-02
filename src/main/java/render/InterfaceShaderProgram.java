package render;

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

}
