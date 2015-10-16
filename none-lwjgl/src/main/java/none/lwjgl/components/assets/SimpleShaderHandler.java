package none.lwjgl.components.assets;

import com.google.inject.Singleton;
import none.engine.component.assets.ShaderHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Simple Shader Handler.
 */
@Singleton
public class SimpleShaderHandler implements ShaderHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleShaderHandler.class);

    private Map<String, Integer> vertexShader;
    private Map<String, Integer> fragmentShader;
    private List<Integer> programs;

    public SimpleShaderHandler() {
        this.vertexShader = new HashMap<>();
        this.fragmentShader = new HashMap<>();
        this.programs = new ArrayList<>();
    }

    @Override
    public void loadVertexShader(String path) {
        LOGGER.info("Load Vertex-Shader: {}", path);
        checkShaderLoadValid(vertexShader, path);
        int vertexId = loadShader(path, GL20.GL_VERTEX_SHADER);
        vertexShader.put(path, vertexId);
    }

    @Override
    public void loadFragmentShader(String path) {
        LOGGER.info("Load Fragment-Shader: {}", path);
        checkShaderLoadValid(fragmentShader, path);
        int fragmentId = loadShader(path, GL20.GL_FRAGMENT_SHADER);
        fragmentShader.put(path, fragmentId);
    }

    private void checkShaderLoadValid(Map<String, Integer> shaderMap, String path) {
        if (shaderMap.get(path) != null) {
            throw new IllegalStateException("The Shader was already loaded.");
        }
    }

    private int loadShader(String path, int shaderType) {
        StringBuilder shaderSource = new StringBuilder();

        try {
            InputStream stream = SimpleShaderHandler.class.getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            for (String input = reader.readLine(); input != null; input = reader.readLine()) {
                shaderSource.append(input).append('\n');
            }

            reader.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Unknown Shader-File", e);
        }

        int shaderId = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShader(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            int length = GL20.glGetShader(shaderId, GL20.GL_INFO_LOG_LENGTH);
            String infoLog = GL20.glGetShaderInfoLog(shaderId, length);

            throw new IllegalArgumentException(path + " failed due to error:\n" + infoLog);
        }

        return shaderId;
    }

    @Override
    public int createProgram() {
        if (vertexShader.isEmpty() || fragmentShader.isEmpty()) {
            throw new IllegalStateException("You have to add Vertex and Fragment Shader first.");
        }
        int programId = GL20.glCreateProgram();
        programs.add(programId);

        LOGGER.info("Create OpenGL-Program {}, Vertex:{} Fragment:{}", programId, vertexShader.keySet(), fragmentShader.keySet());

        attachShader(vertexShader, programId);
        attachShader(fragmentShader, programId);

        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);

        deleteShader(vertexShader);
        deleteShader(fragmentShader);

        int errorCheckValue = GL11.glGetError();
        if (errorCheckValue != GL11.GL_NO_ERROR) {
            throw new IllegalArgumentException("ERROR - Could not create the shaders:" + GLU.gluErrorString(errorCheckValue));
        }

        vertexShader.clear();
        fragmentShader.clear();

        return programId;
    }

    private void attachShader(Map<String, Integer> shader, int programId) {
        for (Map.Entry<String, Integer> entry : shader.entrySet()) {
            GL20.glAttachShader(programId, entry.getValue());
        }
    }

    private void deleteShader(Map<String, Integer> shader) {
        for (Map.Entry<String, Integer> entry : shader.entrySet()) {
            GL20.glDeleteShader(entry.getValue());
        }
    }

    @Override
    public void disposeProgram(int programId) {
        if (!programs.contains(programId)) {
            throw new IllegalStateException("Given Program-Id where never loaded.");
        }
        LOGGER.info("Dispose OpenGL-Program {}", programId);
        GL20.glDeleteProgram(programId);
    }
}
