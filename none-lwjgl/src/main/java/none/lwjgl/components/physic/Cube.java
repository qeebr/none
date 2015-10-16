package none.lwjgl.components.physic;

import none.engine.component.physic.Face;
import none.engine.component.physic.RigidBody;
import org.joml.Vector3d;

/**
 * Cube used for AABB.
 */
public class Cube {
    private double xMin = Double.MAX_VALUE;
    private double xMax = Double.MIN_VALUE;
    private double yMin = Double.MAX_VALUE;
    private double yMax = Double.MIN_VALUE;
    private double zMin = Double.MAX_VALUE;
    private double zMax = Double.MIN_VALUE;

    public Cube(Vector3d transform, RigidBody body) {
        for (Face face : body.getFaces()) {
            for (Vector3d vertex : face.getVertices()) {
                if (vertex.x < xMin) {
                    xMin = vertex.x;
                }
                if (vertex.x > xMax) {
                    xMax = vertex.x;
                }

                if (vertex.y < yMin) {
                    yMin = vertex.y;
                }
                if (vertex.y > yMax) {
                    yMax = vertex.y;
                }

                if (vertex.z < zMin) {
                    zMin = vertex.z;
                }
                if (vertex.z > zMax) {
                    zMax = vertex.z;
                }
            }
        }

        xMin += transform.x;
        xMax += transform.x;

        yMin += transform.y;
        yMax += transform.y;

        zMin += transform.z;
        zMax += transform.z;
    }

    public boolean intersects(Cube other) {
        return ((xMin <= other.xMin && other.xMin <= xMax) || (other.xMin <= xMin && xMin <= other.xMax)) &&
                ((yMin <= other.yMin && other.yMin <= yMax) || (other.yMin <= yMin && yMin <= other.yMax)) &&
                ((zMin <= other.zMin && other.zMin <= zMax) || (other.zMin <= zMin && zMin <= other.zMax));
    }
}
