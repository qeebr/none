package none.engine.component.assets;

import none.engine.component.renderer.Mesh;

/**
 * Handles Loading in Graphic-Framework.
 */
public interface MeshHandler<T extends Mesh> {

    /**
     * Handles the loading from a model.
     *
     * @param path Path of model.
     * @return id for loaded model.
     */
    T loadMesh(String path);

    /**
     * Handles disposing from a model.
     *
     * @param mesh Mesh to dispose.
     */
    void disposeMesh(Mesh mesh);
}
