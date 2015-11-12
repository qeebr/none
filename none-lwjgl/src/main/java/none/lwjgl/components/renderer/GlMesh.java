package none.lwjgl.components.renderer;

import com.google.common.base.Preconditions;
import none.engine.component.model.Model;
import none.engine.component.renderer.Mesh;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Every data needed for a mesh in OpenGL.
 */
public class GlMesh extends Mesh {
    public static final String NAME = "GlMesh";

    private final int vertexArrayId;
    private final int[] buffers;

    public GlMesh(UUID uuid, Model model, int vertexArrayId, List<Integer> generatedBuffers) {
        super(NAME, uuid, model);

        this.vertexArrayId = vertexArrayId;

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

    public int[] getBuffers() {
        return Arrays.copyOf(buffers, buffers.length);
    }
}
