package none.engine.component.physic;

import none.engine.component.AbsObject;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A RigidBody.
 */
public abstract class RigidBody extends AbsObject {
    public static final String NAME = RigidBody.class.getSimpleName();

    private List<Face> faces;
    private List<Vector3d> forces;
    private double mass;
    private Vector3d velocity;
    private boolean onFloor;
    private Type type;
    private String filePath;

    protected RigidBody(UUID id, String filePath, Type type, List<Face> faces) {
        super(NAME, id);

        this.forces = new ArrayList<>();
        this.velocity = new Vector3d();
        this.mass = 1;

        this.faces = faces;
        this.filePath = filePath;
        this.type = type;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public double getMass() {
        return mass;
    }

    public List<Vector3d> getForces() {
        return forces;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    public boolean isOnFloor() {
        return onFloor;
    }

    protected void setOnFloor(boolean onFloor) {
        this.onFloor = onFloor;
    }

    public Type getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }

    public enum Type {
        Floor,
        Moveable
    }
}
