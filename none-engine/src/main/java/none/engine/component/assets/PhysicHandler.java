package none.engine.component.assets;

import none.engine.component.physic.RigidBody;

/**
 * Handles Logic to Load RigidBody from Files.
 */
public interface PhysicHandler {
    RigidBody loadMoveableRigidBody(String filePath);

    RigidBody loadFloorRigidBody(String filePath);
}
