package none.engine.component.physic;

import org.joml.Vector3d;

import java.util.List;

/**
 * A single Face.
 */
public class Face {

    private List<Vector3d> vertices;
    private Vector3d normalVector;
    private Vector3d publicNormalVector;

    public Face(List<Vector3d> vertices, Vector3d normalVector) {
        this.vertices = vertices;
        this.normalVector = normalVector;
        this.publicNormalVector = new Vector3d();
    }

    public List<Vector3d> getVertices() {
        return vertices;
    }

    public Vector3d getNormalVector() {
        publicNormalVector.x = normalVector.x;
        publicNormalVector.y = normalVector.y;
        publicNormalVector.z = normalVector.z;

        return publicNormalVector;
    }
}
