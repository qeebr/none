package none.engine.scenes;

import none.engine.component.EngineObject;
import none.engine.component.renderer.camera.CameraComponent;

/**
 * A Scene.
 */
public interface Scene extends EngineObject {

    /**
     * Returns the active Camera.
     *
     * @return CameraComponent.
     */
    CameraComponent getActiveCamera();
}
