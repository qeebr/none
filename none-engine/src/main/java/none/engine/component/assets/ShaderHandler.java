package none.engine.component.assets;

/**
 * Handles loading Shaders in Graphic-Framework.
 */
public interface ShaderHandler {
    /**
     * Loads a vertex shader.
     *
     * @param path path to vertex-shader.
     */
    void loadVertexShader(String path);

    /**
     * Loads a fragment shader.
     *
     * @param path path to fragment-shader
     */
    void loadFragmentShader(String path);

    /**
     * Creates the Shader-program.
     *
     * @return program id.
     */
    int createProgram();

    /**
     * Releases all memory needed by current program.
     */
    void disposeProgram(int programId);
}
