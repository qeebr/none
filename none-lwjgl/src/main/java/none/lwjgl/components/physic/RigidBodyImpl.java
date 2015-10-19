package none.lwjgl.components.physic;

import none.engine.component.physic.Face;
import none.engine.component.physic.RigidBody;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of RigidBody. Makes the setter and ctor visible.
 */
public class RigidBodyImpl extends RigidBody {

    public RigidBodyImpl(UUID id, String filePath, Type type, List<Face> faces) {
        super(id, filePath, type, faces);
    }

    @Override
    public void setOnFloor(boolean onFloor) {
        super.setOnFloor(onFloor);
    }
}
