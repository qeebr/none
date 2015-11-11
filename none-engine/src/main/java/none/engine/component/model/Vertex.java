package none.engine.component.model;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
        this.position = Validate.notNull(position);
        this.normal = Validate.notNull(normal);
        this.texture = Validate.notNull(texture);
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Vertex rhs = (Vertex) obj;
        return new EqualsBuilder()
                .append(this.position, rhs.position)
                .append(this.normal, rhs.normal)
                .append(this.texture, rhs.texture)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(position)
                .append(normal)
                .append(texture)
                .toHashCode();
    }
}
