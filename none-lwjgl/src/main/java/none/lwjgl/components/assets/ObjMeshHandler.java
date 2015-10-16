package none.lwjgl.components.assets;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import none.engine.component.assets.Assets;
import none.engine.component.assets.MeshHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Mesh;
import none.lwjgl.components.renderer.GlMesh;
import org.joml.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loads Object-Files.
 */
@Singleton
public class ObjMeshHandler extends BaseHandler<GlMesh> implements MeshHandler<GlMesh> {
    public static final int VERTEX_BUFFER = 0;
    public static final int COLOR_BUFFER = 1;
    public static final int TEXTURE_BUFFER = 2;
    public static final int NORMAL_BUFFER = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjMeshHandler.class);
    private static final int INVALID = -1;
    private static final int VECTOR_3_SIZE = 3;
    private static final int VECTOR_2_SIZE = 2;

    private static final String FLOATING_PATTERN = "(-?\\d+\\.\\d*)";
    private static final String VERTEX_PREFIX = "v ";
    private static final String NORMAL_PREFIX = "vn ";
    private static final String TEXTURE_PREFIX = "vt ";
    private static final String FACES_PREFIX = "f ";
    private static final Pattern VERTEX_PATTERN = Pattern.compile(VERTEX_PREFIX + FLOATING_PATTERN + " " + FLOATING_PATTERN + " " + FLOATING_PATTERN);
    private static final Pattern NORMAL_PATTERN = Pattern.compile(NORMAL_PREFIX + FLOATING_PATTERN + " " + FLOATING_PATTERN + " " + FLOATING_PATTERN);
    private static final Pattern TEXTURE_PATTERN = Pattern.compile(TEXTURE_PREFIX + FLOATING_PATTERN + " " + FLOATING_PATTERN);
    private static final Pattern FACES_PATTERN = Pattern.compile(FACES_PREFIX + "(\\d+)/(\\d+)/(\\d+) (\\d+)/(\\d+)/(\\d+) (\\d+)/(\\d+)/(\\d+)");

    private static final String CANNOT_IMPORT_MSG = "Cannot import file regex didn't match ";

    private final UUIDFactory uuidFactory;

    @Inject
    public ObjMeshHandler(UUIDFactory uuidFactory, Assets assets) {
        super(assets);
        this.uuidFactory = Preconditions.checkNotNull(uuidFactory, "uuidFactory");
    }

    @Override
    public GlMesh loadMesh(String path) {
        GlMesh mesh = checkLoaded(path);

        if (mesh == null) {
            LOGGER.info("Load Mesh: {}", path);

            mesh = loadModel(path);
            addResource(path, mesh);
        }

        return mesh;
    }

    private GlMesh loadModel(String path) {
        List<Vector3d> blankVertices = new ArrayList<>();
        List<Vector3d> blankUvs = new ArrayList<>();
        List<Vector3d> blankNormals = new ArrayList<>();

        List<Integer> indices = new ArrayList<>();
        List<Vector3d> vertices = new ArrayList<>();
        List<Vector3d> uvs = new ArrayList<>();
        List<Vector3d> normals = new ArrayList<>();

        if (extractFileProperties(path, blankVertices, blankUvs, blankNormals)) {
            return null;
        }

        updateForIndices(blankVertices, blankUvs, blankNormals,
                vertices, uvs, normals, indices);

        FloatBuffer verticesBuffer = createFloatBuffer(vertices, VECTOR_3_SIZE);
        FloatBuffer normalBuffer = createFloatBuffer(normals, VECTOR_3_SIZE);
        FloatBuffer textureBuffer = createFloatBuffer(uvs, VECTOR_2_SIZE);
        ByteBuffer indicesBuffer = createByteBuffer(indices);

        return handoverOpenGl(path, indices, uvs, normals, verticesBuffer, normalBuffer, textureBuffer, indicesBuffer);
    }

    protected GlMesh handoverOpenGl(String path, List<Integer> indices, List<Vector3d> uvs, List<Vector3d> normals,
                                    FloatBuffer verticesBuffer, FloatBuffer normalBuffer, FloatBuffer textureBuffer, ByteBuffer indicesBuffer) {
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        List<Integer> generatedBuffers = new ArrayList<>();

        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_DYNAMIC_DRAW);
        GL20.glEnableVertexAttribArray(VERTEX_BUFFER);
        GL20.glVertexAttribPointer(VERTEX_BUFFER, VECTOR_3_SIZE, GL11.GL_FLOAT, false, 0, 0);
        generatedBuffers.add(vboId);

        int vboTexId = INVALID;
        if (!uvs.isEmpty()) {
            vboTexId = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTexId);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureBuffer, GL15.GL_DYNAMIC_DRAW);
            GL20.glEnableVertexAttribArray(TEXTURE_BUFFER);
            GL20.glVertexAttribPointer(TEXTURE_BUFFER, VECTOR_2_SIZE, GL11.GL_FLOAT, false, 0, 0);
            generatedBuffers.add(vboTexId);
        }

        int vboNormId = INVALID;
        if (!normals.isEmpty()) {
            vboNormId = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboNormId);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalBuffer, GL15.GL_DYNAMIC_DRAW);
            GL20.glEnableVertexAttribArray(NORMAL_BUFFER);
            GL20.glVertexAttribPointer(NORMAL_BUFFER, VECTOR_3_SIZE, GL11.GL_FLOAT, false, 0, 0);
            generatedBuffers.add(vboNormId);
        }

        int indicesId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_DYNAMIC_DRAW);

        GL30.glBindVertexArray(0);

        return new GlMesh(uuidFactory.createUUID(), path, vaoId, indices.size(), indicesId, false, vboTexId != INVALID, vboNormId != INVALID, generatedBuffers);
    }

    private ByteBuffer createByteBuffer(List<Integer> indices) {
        byte[] byteArray = new byte[indices.size()];
        for (int index = 0; index < indices.size(); index++) {
            byteArray[index] = indices.get(index).byteValue();
        }

        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(byteArray.length);
        byteBuffer.put(byteArray);
        byteBuffer.flip();

        return byteBuffer;
    }

    private FloatBuffer createFloatBuffer(List<Vector3d> vector3s, int paramCount) {
        float[] floatArray = new float[vector3s.size() * paramCount];
        for (int index = 0; index < vector3s.size(); index++) {
            Vector3d vector3 = vector3s.get(index);
            int offset = 0;
            floatArray[(index * paramCount) + offset++] = ((float) vector3.x);
            floatArray[(index * paramCount) + offset++] = ((float) vector3.y);
            if (paramCount == VECTOR_3_SIZE) {
                floatArray[(index * paramCount) + offset] = ((float) vector3.z);
            }
        }

        FloatBuffer vectorBuffer = BufferUtils.createFloatBuffer(floatArray.length);
        vectorBuffer.put(floatArray);
        vectorBuffer.flip();

        return vectorBuffer;
    }

    private void updateForIndices(
            List<Vector3d> blankVertices, List<Vector3d> blankUvs, List<Vector3d> blankNormals,
            List<Vector3d> vertices, List<Vector3d> uvs, List<Vector3d> normals, List<Integer> indices) {

        for (int currentIndex = 0; currentIndex < blankVertices.size(); currentIndex++) {
            Vector3d vertex = blankVertices.get(currentIndex);
            Vector3d equalVertex = null;
            int equalIndex = INVALID;

            for (int i = 0; i < vertices.size(); i++) {
                Vector3d knownVertex = vertices.get(i);
                if (knownVertex.equals(vertex)) {
                    equalVertex = knownVertex;
                    equalIndex = i;
                    break;
                }
            }
            if (equalVertex == null) {
                vertices.add(vertex);
                if ((blankUvs.size() + 1) >= currentIndex) {
                    uvs.add(blankUvs.get(currentIndex));
                }
                if ((blankNormals.size() + 1) >= currentIndex) {
                    normals.add(blankNormals.get(currentIndex));
                }
                indices.add(vertices.size() - 1);
            } else {
                indices.add(equalIndex);
            }
        }
    }

    private boolean extractFileProperties(String path, List<Vector3d> vertices, List<Vector3d> uvs, List<Vector3d> normals) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().getClass().getResourceAsStream(path)))) {
            List<Vector3d> tmpVertices = new ArrayList<>();
            List<Vector3d> tmpUV = new ArrayList<>();
            List<Vector3d> tmpNormal = new ArrayList<>();

            List<Integer> vertexIndices = new ArrayList<>();
            List<Integer> uvIndices = new ArrayList<>();
            List<Integer> normalIndices = new ArrayList<>();

            br.lines().forEach(line -> extractLineProperty(tmpVertices, tmpUV, tmpNormal,
                    vertexIndices, uvIndices, normalIndices, line));

            for (int i = 0; i < vertexIndices.size(); i++) {
                int vertexIndex = vertexIndices.get(i);
                int uvIndex = uvIndices.get(i);
                int normalIndex = normalIndices.get(i);

                Vector3d vertex = tmpVertices.get(vertexIndex - 1);
                Vector3d uv = tmpUV.get(uvIndex - 1);
                Vector3d normal = tmpNormal.get(normalIndex - 1);

                vertices.add(vertex);
                uvs.add(uv);
                normals.add(normal);
            }

        } catch (IOException e) {
            LOGGER.error("Exception during loading", e);
            return true;
        }
        return false;
    }

    private void extractLineProperty(List<Vector3d> tmpVertices, List<Vector3d> tmpUV, List<Vector3d> tmpNormal,
                                     List<Integer> vertexIndices, List<Integer> uvIndices, List<Integer> normalIndices,
                                     String line) {
        if (line.startsWith(VERTEX_PREFIX)) {
            extractVertex(tmpVertices, line);
        } else if (line.startsWith(TEXTURE_PREFIX)) {
            extractTexture(tmpUV, line);
        } else if (line.startsWith(NORMAL_PREFIX)) {
            extractNormal(tmpNormal, line);
        } else if (line.startsWith(FACES_PREFIX)) {
            extractFace(vertexIndices, uvIndices, normalIndices, line);
        }
    }

    private void extractFace(List<Integer> vertexIndices, List<Integer> uvIndices, List<Integer> normalIndices, String line) {
        Matcher matcher = FACES_PATTERN.matcher(line);
        if (!matcher.find()) {
            throw new IllegalStateException(CANNOT_IMPORT_MSG + FACES_PATTERN.toString());
        }

        int offset = 0;
        int value = Integer.parseInt(matcher.group(++offset));
        vertexIndices.add(value);
        value = Integer.parseInt(matcher.group(++offset));
        uvIndices.add(value);
        value = Integer.parseInt(matcher.group(++offset));
        normalIndices.add(value);

        value = Integer.parseInt(matcher.group(++offset));
        vertexIndices.add(value);
        value = Integer.parseInt(matcher.group(++offset));
        uvIndices.add(value);
        value = Integer.parseInt(matcher.group(++offset));
        normalIndices.add(value);

        value = Integer.parseInt(matcher.group(++offset));
        vertexIndices.add(value);
        value = Integer.parseInt(matcher.group(++offset));
        uvIndices.add(value);
        value = Integer.parseInt(matcher.group(++offset));
        normalIndices.add(value);
    }

    private void extractNormal(List<Vector3d> tmpNormal, String line) {
        Matcher matcher = NORMAL_PATTERN.matcher(line);
        if (!matcher.find()) {
            throw new IllegalStateException(CANNOT_IMPORT_MSG + NORMAL_PATTERN.toString());
        }
        extractGroups(tmpNormal, matcher, VECTOR_3_SIZE);
    }

    private void extractTexture(List<Vector3d> tmpUV, String line) {
        Matcher matcher = TEXTURE_PATTERN.matcher(line);
        if (!matcher.find()) {
            throw new IllegalStateException(CANNOT_IMPORT_MSG + TEXTURE_PATTERN.toString());
        }
        extractGroups(tmpUV, matcher, VECTOR_2_SIZE);
    }

    private void extractVertex(List<Vector3d> tmpVertices, String line) {
        Matcher matcher = VERTEX_PATTERN.matcher(line);
        if (!matcher.find()) {
            throw new IllegalStateException(CANNOT_IMPORT_MSG + VERTEX_PATTERN.toString());
        }
        extractGroups(tmpVertices, matcher, VECTOR_3_SIZE);
    }

    private void extractGroups(List<Vector3d> vectorList, Matcher matcher, int vectorSize) {
        int offset = 0;
        vectorList.add(new Vector3d(
                Double.parseDouble(matcher.group(++offset)),
                Double.parseDouble(matcher.group(++offset)),
                vectorSize == VECTOR_3_SIZE ? Double.parseDouble(matcher.group(++offset)) : 0.0));
    }

    @Override
    public void disposeMesh(Mesh mesh) {
        if (!(mesh instanceof GlMesh)) {
            throw new IllegalStateException("To disposing mesh is not a GlMesh.");
        }

        disposeMesh((GlMesh) mesh);
    }

    private void disposeMesh(GlMesh mesh) {
        if (dispose(mesh, mesh.getModelPath())) {
            LOGGER.info("Dispose Mesh: {}", mesh.getModelPath());

            disposeData(mesh);
        }
    }

    private void disposeData(GlMesh data) {
        GL30.glBindVertexArray(data.getVertexArrayId());

        GL20.glDisableVertexAttribArray(VERTEX_BUFFER);
        if (data.isHasColor()) {
            GL20.glDisableVertexAttribArray(COLOR_BUFFER);
        }
        if (data.isHasTexture()) {
            GL20.glDisableVertexAttribArray(TEXTURE_BUFFER);
        }
        if (data.isHasNorm()) {
            GL20.glDisableVertexAttribArray(NORMAL_BUFFER);
        }

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        for (Integer buffer : data.getBuffers()) {
            GL15.glDeleteBuffers(buffer);
        }

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(data.getIndicesBufferId());


        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(data.getVertexArrayId());
    }
}
