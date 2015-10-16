package none.engine.component.renderer;

import none.engine.Game;
import none.engine.component.AbsObject;
import org.joml.Vector3d;

import java.util.Objects;
import java.util.UUID;

/**
 * A Renderer for Camera related actions.
 */
public abstract class CameraComponent extends AbsObject {
    private Vector3d cameraPosition;
    private Vector3d objectPosition;
    private Vector3d upDirection;

    public CameraComponent(String name, UUID id, Game game) {
        super(name, id, game);

        cameraPosition = new Vector3d();
        objectPosition = new Vector3d(0, 0, -1);
        upDirection = new Vector3d(0, 1, 0);
    }

    public Vector3d getCameraPosition() {
        return cameraPosition;
    }

    public Vector3d getObjectPosition() {
        return objectPosition;
    }

    public Vector3d getUpDirection() {
        return upDirection;
    }

    public void lookAt(Vector3d cameraPosition, Vector3d objectPosition, Vector3d upDirection) {
        this.cameraPosition = Objects.requireNonNull(cameraPosition);
        this.objectPosition = Objects.requireNonNull(objectPosition);
        this.upDirection = Objects.requireNonNull(upDirection);
    }
}
