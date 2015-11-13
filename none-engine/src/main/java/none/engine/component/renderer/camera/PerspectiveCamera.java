package none.engine.component.renderer.camera;

import none.engine.Game;
import none.engine.GameOptions;

import java.util.UUID;

/**
 * A camera using perspective projection.
 */
public class PerspectiveCamera extends Camera {

    private float fieldOfView;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

    public PerspectiveCamera(UUID id, Game game) {
        super(PerspectiveCamera.class.getSimpleName(), id, game);
    }

    public float getFieldOfView() {
        return fieldOfView;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public float getNearPlane() {
        return nearPlane;
    }

    public float getFarPlane() {
        return farPlane;
    }

    @Override
    public void init() {
        GameOptions options = getGame().getOptions();

        this.fieldOfView = options.getFieldOfView();
        this.aspectRatio = (float) options.getDisplayWidth() / options.getDisplayHeight();
        this.nearPlane = options.getNearPlane();
        this.farPlane = options.getFarPlane();
    }
}
