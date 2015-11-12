package none.engine.component.assets;

import none.engine.component.model.Model;
import none.engine.component.renderer.Mesh;

/**
 * Handles Loading in Graphic-Framework.
 */
public interface MeshHandler<T extends Mesh> {

    /**
     * Handles loading from a model.
     *
     * @param model Model.
     * @return id for loaded model.
     */
    T loadMesh(Model model);

    /**
     * Handles disposing from a model.
     *
     * @param mesh Mesh to dispose.
     */
    void disposeMesh(Mesh mesh);
}
