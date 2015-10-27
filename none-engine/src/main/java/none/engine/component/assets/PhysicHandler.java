package none.engine.component.assets;

import none.engine.component.physic.RigidBody;

/**
 * Handles Logic to Load RigidBody from Files.
 */
public interface PhysicHandler {

    /**
     * Loads an moveable RigidBody.
     *
     * @param filePath Path to file.
     * @return Loaded RigidBody.
     */
    RigidBody loadMoveableRigidBody(String filePath);

    /**
     * Loads an solid RigidBody.
     *
     * @param filePath Path to file.
     * @return Loaded RigidBody.
     */
    RigidBody loadFloorRigidBody(String filePath);
}
