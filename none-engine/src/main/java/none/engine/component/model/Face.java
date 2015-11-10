package none.engine.component.model;

import org.apache.commons.lang3.Validate;

/**
 * A Face.
 */
public class Face {
    public static final int VERTEX_COUNT = 3;

    private final Vertex[] faces;

    public Face(Vertex[] faces) {
        Validate.isTrue(faces.length == VERTEX_COUNT, "Face only accept Triangles.");
        this.faces = faces;
    }

    public Vertex[] getVertices() {
        return faces;
    }
}
