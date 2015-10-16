package none.lwjgl.components.renderer;

import com.google.common.base.Preconditions;
import none.engine.component.renderer.Mesh;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Every data needed for a mesh in OpenGL.
 */
public class GlMesh extends Mesh {
    public static final String NAME = "GlMesh";
    private int vertexArrayId;

    private int indicesCount;
    private int indicesBufferId;

    private boolean hasColor;
    private boolean hasTexture;
    private boolean hasNorm;

    private int[] buffers;

    public GlMesh(UUID id, String modelPath,
                  int vertexArrayId, int indicesCount, int indicesBufferId,
                  boolean hasColor, boolean hasTexture, boolean hasNorm,
                  List<Integer> generatedBuffers) {
        super(NAME, id, modelPath);
        this.vertexArrayId = vertexArrayId;
        this.indicesCount = indicesCount;
        this.indicesBufferId = indicesBufferId;
        this.hasColor = hasColor;
        this.hasTexture = hasTexture;
        this.hasNorm = hasNorm;

        Preconditions.checkNotNull(generatedBuffers);
        buffers = new int[generatedBuffers.size()];
        int i = 0;
        for (Integer buffer : generatedBuffers) {
            buffers[i++] = buffer;
        }
    }

    public int getVertexArrayId() {
        return vertexArrayId;
    }

    public int getIndicesCount() {
        return indicesCount;
    }

    public int getIndicesBufferId() {
        return indicesBufferId;
    }

    public boolean isHasColor() {
        return hasColor;
    }

    public boolean isHasTexture() {
        return hasTexture;
    }

    public boolean isHasNorm() {
        return hasNorm;
    }

    public int[] getBuffers() {
        return Arrays.copyOf(buffers, buffers.length);
    }
}
