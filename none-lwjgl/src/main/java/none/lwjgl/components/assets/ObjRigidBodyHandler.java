package none.lwjgl.components.assets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import none.engine.component.assets.Assets;
import none.engine.component.assets.PhysicHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.physic.Face;
import none.engine.component.physic.RigidBody;
import none.lwjgl.components.physic.RigidBodyImpl;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loads RigidBodies from Object-Files.
 */
@Singleton
public class ObjRigidBodyHandler extends BaseHandler<RigidBody, String> implements PhysicHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(ObjRigidBodyHandler.class);
    private final UUIDFactory uuidFactory;


    @Inject
    public ObjRigidBodyHandler(UUIDFactory uuidFactory, Assets assets) {
        super(assets);
        this.uuidFactory = Objects.requireNonNull(uuidFactory, "uuidFactory");
    }

    @Override
    public RigidBody loadMoveableRigidBody(String filePath) {
        LOGGER.info("Create Moveable-RigidBody: {}", filePath);

        return loadBody(filePath, RigidBody.Type.Moveable);
    }

    @Override
    public RigidBody loadFloorRigidBody(String filePath) {
        LOGGER.info("Create Floor-RigidBody: {}", filePath);

        return loadBody(filePath, RigidBody.Type.Floor);
    }

    private RigidBody loadBody(String filePath, RigidBody.Type type) {
        List<Vector3d> vertices = new ArrayList<>();
        List<Vector3d> normals = new ArrayList<>();
        List<Integer> vertexIndex = new ArrayList<>();
        List<Integer> normalIndex = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().getClass().getResourceAsStream(filePath)))) {
            br.lines().forEach(line -> {
                Pattern vertexNormalPattern = Pattern.compile("(v|vn) (-?\\d+.\\d+) (-?\\d+.\\d+) (-?\\d+.\\d+)");
                Pattern facesPattern = Pattern.compile("f (\\d+)/\\d+/(\\d+) (\\d+)/\\d+/(\\d+) (\\d+)/\\d+/(\\d+)");

                if (line.startsWith("v ")) {
                    Matcher matcher = vertexNormalPattern.matcher(line);
                    if (!matcher.find()) {
                        throw new IllegalStateException("Vertex has to be accepted from Parser");
                    }
                    double x = Double.valueOf(matcher.group(2));
                    double y = Double.valueOf(matcher.group(3));
                    double z = Double.valueOf(matcher.group(4));

                    vertices.add(new Vector3d(x, y, z));
                } else if (line.startsWith("vn")) {
                    Matcher matcher = vertexNormalPattern.matcher(line);
                    if (!matcher.find()) {
                        throw new IllegalStateException("Normal has to be accepted from Parser");
                    }
                    double x = Double.valueOf(matcher.group(2));
                    double y = Double.valueOf(matcher.group(3));
                    double z = Double.valueOf(matcher.group(4));

                    normals.add(new Vector3d(x, y, z));
                } else if (line.startsWith("f")) {
                    Matcher matcher = facesPattern.matcher(line);
                    if (!matcher.find()) {
                        throw new IllegalStateException("Face has to be accepted from Parser");
                    }
                    vertexIndex.add(Integer.valueOf(matcher.group(1)));
                    vertexIndex.add(Integer.valueOf(matcher.group(3)));
                    vertexIndex.add(Integer.valueOf(matcher.group(5)));

                    normalIndex.add(Integer.valueOf(matcher.group(2)));
                    normalIndex.add(Integer.valueOf(matcher.group(4)));
                    normalIndex.add(Integer.valueOf(matcher.group(6)));
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException("Could not load RigidBody: " + filePath, e);
        }

        List<Face> faces = new ArrayList<>();

        for (int index = 0; index < vertexIndex.size(); index += 3) {
            List<Vector3d> faceVertices = new ArrayList<>();
            faceVertices.add(vertices.get(vertexIndex.get(index) - 1));
            faceVertices.add(vertices.get(vertexIndex.get(index + 1) - 1));
            faceVertices.add(vertices.get(vertexIndex.get(index + 2) - 1));

            Vector3d normalV1 = normals.get(normalIndex.get(index) - 1);
            Vector3d normalV2 = normals.get(normalIndex.get(index + 1) - 1);
            Vector3d normalV3 = normals.get(normalIndex.get(index + 2) - 1);

            Vector3d edge1 = normalV2.sub(normalV1);
            Vector3d edge2 = normalV3.sub(normalV1);

            Vector3d cross = edge1.cross(edge2);

            Vector3d faceNormal = cross.normalize();

            faces.add(new Face(faceVertices, faceNormal));
        }

        return new RigidBodyImpl(uuidFactory.createUUID(), filePath, type, faces);
    }
}
