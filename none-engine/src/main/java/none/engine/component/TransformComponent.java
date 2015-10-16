package none.engine.component;

import com.google.common.base.Preconditions;
import none.engine.Game;
import org.joml.Vector3d;

import java.util.Objects;
import java.util.UUID;

/**
 * Transform Component. Locates parent-EngineObject in room.
 */
public class TransformComponent extends AbsObject {
    public static final String NAME = "Transform";

    private Vector3d position;
    private Vector3d direction;

    private float rotX;
    private float rotY;
    private float rotZ;

    public TransformComponent(UUID objectId, Game game, EngineObject parent, Vector3d position, Vector3d direction) {
        super(NAME, objectId, game, parent);
        this.position = Preconditions.checkNotNull(position, "position");
        this.direction = Preconditions.checkNotNull(direction, "direction");

        this.rotX = 0f;
        this.rotY = 0f;
        this.rotZ = 0f;
    }

    public TransformComponent(UUID objectId, Vector3d position, Vector3d direction) {
        super(NAME, objectId);
        this.position = Objects.requireNonNull(position);
        this.direction = Objects.requireNonNull(direction);

        this.rotX = 0f;
        this.rotY = 0f;
        this.rotZ = 0f;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = Preconditions.checkNotNull(position);
    }

    public Vector3d getDirection() {
        return direction;
    }

    public void setDirection(Vector3d direction) {
        this.direction = Preconditions.checkNotNull(direction);
    }
}
