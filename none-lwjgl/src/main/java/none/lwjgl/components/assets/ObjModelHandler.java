package none.lwjgl.components.assets;

import com.google.inject.Inject;
import none.engine.component.assets.Assets;
import none.engine.component.assets.ModelHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.model.Face;
import none.engine.component.model.Model;
import none.engine.component.model.Vertex;
import org.apache.commons.lang3.Validate;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loads a Model from an object file.
 */
public class ObjModelHandler extends BaseHandler<Model> implements ModelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjModelHandler.class);
    private static final Pattern VERTEX_NORMAL_PATTERN = Pattern.compile("(v|vn) (-?\\d+.\\d+) (-?\\d+.\\d+) (-?\\d+.\\d+)");
    private static final Pattern TEXTURE_PATTERN = Pattern.compile("vt (-?\\d+.\\d+) (-?\\d+.\\d+)");
    private static final Pattern FACES_PATTERN = Pattern.compile("f (\\d+)/(\\d+)/(\\d+) (\\d+)/(\\d+)/(\\d+) (\\d+)/(\\d+)/(\\d+)");
    private static final int VERTEX_OFFSET = 0;
    private static final int TEXTURE_OFFSET = 1;
    private static final int NORMAL_OFFSET = 2;

    private final UUIDFactory uuidFactory;

    @Inject
    protected ObjModelHandler(UUIDFactory uuidFactory, Assets assets) {
        super(assets);

        this.uuidFactory = Validate.notNull(uuidFactory);
    }

    @Override
    public Model loadModel(String path) {
        Model model = checkLoaded(path);

        if (model == null) {
            LOGGER.info("Load Mesh: {}", path);

            model = loadModelImpl(path);
            addResource(path, model);
        }

        return model;
    }

    private Model loadModelImpl(String path) {
        List<String> fileContent = loadFileContent(path);

        List<Vector3d> primVertices = new ArrayList<>();
        List<Vector3d> primNormals = new ArrayList<>();
        List<Vector2d> primTextures = new ArrayList<>();
        extractPrimitives(path, fileContent, primVertices, primNormals, primTextures);

        List<Integer> primFaces = new ArrayList<>();
        extractPrimitiveFaces(path, fileContent, primFaces);

        List<Face> faces = new ArrayList<>();
        List<Vertex> vertices = new ArrayList<>();
        refineVerticesAndFaces(faces, vertices, primVertices, primNormals, primTextures, primFaces);

        return new Model(uuidFactory.createUUID(), path, faces, vertices);
    }

    private void refineVerticesAndFaces(List<Face> faces, List<Vertex> vertices,
                                        List<Vector3d> primVertices, List<Vector3d> primNormals, List<Vector2d> primTextures,
                                        List<Integer> primFaces) {
        int faceIndex = 0;
        Vertex[] faceVertices = new Vertex[Face.VERTEX_COUNT];
        Set<Vertex> verticesSet = new HashSet<>();

        //Every Triple is a single Vertex.
        for (int primFaceIndex = 0; primFaceIndex < primFaces.size(); primFaceIndex += 3) {

            Vector3d position = primVertices.get(primFaces.get(primFaceIndex + VERTEX_OFFSET) - 1);
            Vector2d texture = primTextures.get(primFaces.get(primFaceIndex + TEXTURE_OFFSET) - 1);
            Vector3d normal = primNormals.get(primFaces.get(primFaceIndex + NORMAL_OFFSET) - 1);

            Vertex vertex = new Vertex(position, normal, texture);
            verticesSet.add(vertex); //Add Vertex to set. Maybe enhance Vertex to add all Faces using this Vertex.

            //Check if 3 Vertex are found -> Which builds a Face.
            faceVertices[faceIndex++] = vertex;
            if (faceIndex == Face.VERTEX_COUNT) {
                faces.add(new Face(faceVertices));

                faceIndex = 0;
                faceVertices = new Vertex[Face.VERTEX_COUNT];
            }
        }

        vertices.addAll(verticesSet);
    }

    private void extractPrimitiveFaces(String path, List<String> fileContent,
                                       List<Integer> faces) {
        for (String line : fileContent) {
            if (line.startsWith("f ")) {
                Matcher matcher = FACES_PATTERN.matcher(line);
                if (!(matcher.find() && matcher.matches())) {
                    String errorMessage = buildIllegalFormatString(path, line, FACES_PATTERN.toString());
                    LOGGER.error(errorMessage);
                    throw new IllegalStateException(errorMessage);
                }

                int value = Integer.parseInt(matcher.group(1));
                faces.add(value);
                value = Integer.parseInt(matcher.group(2));
                faces.add(value);
                value = Integer.parseInt(matcher.group(3));
                faces.add(value);

                value = Integer.parseInt(matcher.group(4));
                faces.add(value);
                value = Integer.parseInt(matcher.group(5));
                faces.add(value);
                value = Integer.parseInt(matcher.group(6));
                faces.add(value);

                value = Integer.parseInt(matcher.group(7));
                faces.add(value);
                value = Integer.parseInt(matcher.group(8));
                faces.add(value);
                value = Integer.parseInt(matcher.group(9));
                faces.add(value);
            }
        }
    }

    private void extractPrimitives(String path, List<String> fileContent,
                                   List<Vector3d> vertices, List<Vector3d> normals, List<Vector2d> textures) {
        for (String line : fileContent) {
            if (line.startsWith("v ")) {
                extractVertexOrNormal(path, line, vertices);
            } else if (line.startsWith("vn ")) {
                extractVertexOrNormal(path, line, normals);
            } else if (line.startsWith("vt ")) {
                extractTexture(path, line, textures);
            }
        }
    }

    private void extractTexture(String path, String line, List<Vector2d> textures) {
        Matcher matcher = TEXTURE_PATTERN.matcher(line);

        if (!(matcher.find() && matcher.matches())) {
            String errorMessage = buildIllegalFormatString(path, line, TEXTURE_PATTERN.toString());
            LOGGER.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        double x = Double.valueOf(matcher.group(1));
        double y = Double.valueOf(matcher.group(2));

        textures.add(new Vector2d(x, y));
    }

    private void extractVertexOrNormal(String path, String line, List<Vector3d> primitive) {
        Matcher matcher = VERTEX_NORMAL_PATTERN.matcher(line);

        if (!(matcher.find() && matcher.matches())) {
            String errorMessage = buildIllegalFormatString(path, line, VERTEX_NORMAL_PATTERN.toString());
            LOGGER.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        double x = Double.valueOf(matcher.group(2));
        double y = Double.valueOf(matcher.group(3));
        double z = Double.valueOf(matcher.group(4));

        primitive.add(new Vector3d(x, y, z));
    }

    protected List<String> loadFileContent(String path) {
        try {
            return Files.readAllLines(Paths.get(getAssets().getClass().getResource(path).toURI()));
        } catch (NullPointerException | URISyntaxException | IOException e) {
            String errorMessage = "File " + path + " should be loadable.";
            LOGGER.error(errorMessage, e);
            throw new IllegalStateException(errorMessage, e);
        }
    }

    private String buildIllegalFormatString(String path, String line, String pattern) {
        return "File: " + path + " Line: " + line + " did not match: " + pattern;
    }

    @Override
    public void disposeModel(Model model) {
        if (dispose(model, model.getSourcePath())) {
            LOGGER.info("Dispose Mesh: {}", model.getSourcePath());
        }
    }
}
