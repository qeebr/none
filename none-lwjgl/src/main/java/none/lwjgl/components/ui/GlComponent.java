package none.lwjgl.components.ui;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A small class, which contains information's for OpenGl.
 */
public class GlComponent {

    public static final int WINDOWS_SIZE = 54;

    private int vaoId;
    private int verticesId;
    private int uVId;

    private List<Vector3f> vertices;
    private List<Vector2f> uVs;
    private FloatBuffer verticesBuffer;
    private FloatBuffer uVsBuffer;

    public GlComponent() {
        verticesBuffer = BufferUtils.createFloatBuffer(WINDOWS_SIZE * 3);
        uVsBuffer = BufferUtils.createFloatBuffer(WINDOWS_SIZE * 2);

        vertices = new ArrayList<>(WINDOWS_SIZE);
        uVs = new ArrayList<>(WINDOWS_SIZE);

        for (int i = 0; i < WINDOWS_SIZE; i++) {
            vertices.add(new Vector3f());
            uVs.add(new Vector2f());
        }

        vaoId = GL30.glGenVertexArrays();
        verticesId = GL15.glGenBuffers();
        uVId = GL15.glGenBuffers();
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVerticesId() {
        return verticesId;
    }

    public int getuVId() {
        return uVId;
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Vector2f> getuVs() {
        return uVs;
    }

    public FloatBuffer getVerticesBuffer() {
        return verticesBuffer;
    }

    public FloatBuffer getuVsBuffer() {
        return uVsBuffer;
    }

    public void init() {
        GL30.glBindVertexArray(vaoId);

        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticesId);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        GL20.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uVId);
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 0, 0);

        GL30.glBindVertexArray(0);
    }

    public void dispose() {
        GL15.glDeleteBuffers(uVId);
        GL15.glDeleteBuffers(verticesId);
        GL30.glDeleteVertexArrays(vaoId);
    }
}
