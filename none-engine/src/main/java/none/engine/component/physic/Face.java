package none.engine.component.physic;

import org.joml.Vector3d;

import java.util.List;

/**
 * A single Face.
 */
public class Face {

    private List<Vector3d> vertices;
    private Vector3d normalVector;

    public Face(List<Vector3d> vertices, Vector3d normalVector) {
        this.vertices = vertices;
        this.normalVector = normalVector;
    }

    public List<Vector3d> getVertices() {
        return vertices;
    }

    public Vector3d getNormalVector() {
        return normalVector;
    }
}
