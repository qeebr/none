package none.engine.component.assets;

import none.engine.component.model.Model;

/**
 * Loads Models, a abstract representation for an 3d model.
 * Is used to export Mesh and RigidBody.
 */
public interface ModelHandler {
    /**
     * Loads an Model from the filesystem.
     *
     * @param path Path to model.
     * @return Loaded Model.
     */
    Model loadModel(String path);

    /**
     * Deletes Model from Handler-Cache.
     *
     * @param model Model to be disposed.
     */
    void disposeModel(Model model);
}
