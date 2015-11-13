package none.lwjgl.components.assets;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import none.engine.component.assets.Assets;
import none.engine.component.assets.MeshHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.model.Face;
import none.engine.component.model.Model;
import none.engine.component.model.Vertex;
import none.engine.component.renderer.primitives.Mesh;
import none.lwjgl.components.renderer.GlMesh;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads Object-Files.
 */
@Singleton
public class MeshHandlerImpl extends BaseHandler<GlMesh, Model> implements MeshHandler<GlMesh> {
    public static final int VERTEX_BUFFER = 0;
    public static final int TEXTURE_BUFFER = 2;
    public static final int NORMAL_BUFFER = 3;

    private static final Logger LOGGER = LoggerFactory.getLogger(MeshHandlerImpl.class);
    private static final int VECTOR_3_SIZE = 3;
    private static final int VECTOR_2_SIZE = 2;

    private final UUIDFactory uuidFactory;

    @Inject
    public MeshHandlerImpl(UUIDFactory uuidFactory, Assets assets) {
        super(assets);
        this.uuidFactory = Preconditions.checkNotNull(uuidFactory, "uuidFactory");
    }

    @Override
    public GlMesh loadMesh(Model model) {
        GlMesh mesh = checkLoaded(model);

        if (mesh == null) {
            LOGGER.info("Load Mesh: {}", model);

            mesh = loadModel(model);
            addResource(model, mesh);
        }

        return mesh;
    }

    private GlMesh loadModel(Model model) {
        FloatBuffer verticeBuffer = BufferUtils.createFloatBuffer(model.getFaces().size() * Face.VERTEX_COUNT * 3);
        FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(model.getFaces().size() * Face.VERTEX_COUNT * 3);
        FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(model.getFaces().size() * Face.VERTEX_COUNT * 2);

        fillBuffers(model, verticeBuffer, normalsBuffer, textureBuffer);

        return handoverOpenGl(model, verticeBuffer, normalsBuffer, textureBuffer);
    }

    private void fillBuffers(Model model, FloatBuffer verticeBuffer, FloatBuffer normalsBuffer, FloatBuffer textureBuffer) {
        for (Face face : model.getFaces()) {
            Vertex[] vertexArray = face.getVertices();
            for (Vertex vertex : vertexArray) {
                //Handle position
                verticeBuffer.put((float) vertex.getPosition().x);
                verticeBuffer.put((float) vertex.getPosition().y);
                verticeBuffer.put((float) vertex.getPosition().z);

                //Handle normals
                normalsBuffer.put((float) vertex.getNormal().x);
                normalsBuffer.put((float) vertex.getNormal().y);
                normalsBuffer.put((float) vertex.getNormal().z);

                //handles textures
                textureBuffer.put((float) vertex.getTexture().x);
                textureBuffer.put((float) vertex.getTexture().y);
            }
        }

        verticeBuffer.flip();
        normalsBuffer.flip();
        textureBuffer.flip();
    }

    private GlMesh handoverOpenGl(Model model, FloatBuffer verticeBuffer, FloatBuffer normalsBuffer, FloatBuffer textureBuffer) {
        List<Integer> generatedBuffers = new ArrayList<>();

        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        int vboId = GL15.glGenBuffers();
        GL20.glEnableVertexAttribArray(VERTEX_BUFFER);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticeBuffer, GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(VERTEX_BUFFER, VECTOR_3_SIZE, GL11.GL_FLOAT, false, 0, 0);
        generatedBuffers.add(vboId);

        int vboNormId = GL15.glGenBuffers();
        GL20.glEnableVertexAttribArray(NORMAL_BUFFER);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboNormId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(NORMAL_BUFFER, VECTOR_3_SIZE, GL11.GL_FLOAT, false, 0, 0);
        generatedBuffers.add(vboNormId);

        int vboTexId = GL15.glGenBuffers();
        GL20.glEnableVertexAttribArray(TEXTURE_BUFFER);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTexId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureBuffer, GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(TEXTURE_BUFFER, VECTOR_2_SIZE, GL11.GL_FLOAT, false, 0, 0);
        generatedBuffers.add(vboTexId);

        GL30.glBindVertexArray(0);

        return new GlMesh(uuidFactory.createUUID(), model, vaoId, generatedBuffers);
    }

    @Override
    public void disposeMesh(Mesh mesh) {
        if (!(mesh instanceof GlMesh)) {
            throw new IllegalStateException("To disposing mesh is not a GlMesh.");
        }

        disposeMesh((GlMesh) mesh);
    }

    private void disposeMesh(GlMesh mesh) {
        if (dispose(mesh, mesh.getModel())) {
            LOGGER.info("Dispose Mesh: {}", mesh.getModel());

            disposeData(mesh);
        }
    }

    private void disposeData(GlMesh data) {
        GL30.glBindVertexArray(data.getVertexArrayId());

        GL20.glDisableVertexAttribArray(VERTEX_BUFFER);
        GL20.glDisableVertexAttribArray(TEXTURE_BUFFER);
        GL20.glDisableVertexAttribArray(NORMAL_BUFFER);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        for (Integer buffer : data.getBuffers()) {
            GL15.glDeleteBuffers(buffer);
        }

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(data.getVertexArrayId());
    }
}
