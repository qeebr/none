package none.engine.scenes;

import none.engine.component.EngineObject;
import none.engine.component.renderer.camera.Camera;

/**
 * A Scene.
 */
public interface Scene extends EngineObject {

    /**
     * Returns the active Camera.
     *
     * @return Camera.
     */
    Camera getActiveCamera();
}
