package none.engine.component.model;

import org.joml.Vector2d;
import org.joml.Vector3d;

/**
 * A Vertex. Contains Position, Normal-Vector and Texture coordinates.
 */
public class Vertex {
    private final Vector3d position;
    private final Vector3d normal;
    private final Vector2d texture;

    public Vertex(Vector3d position, Vector3d normal, Vector2d texture) {
        this.position = position;
        this.normal = normal;
        this.texture = texture;
    }

    public Vector3d getPosition() {
        return position;
    }

    public Vector3d getNormal() {
        return normal;
    }

    public Vector2d getTexture() {
        return texture;
    }
}
