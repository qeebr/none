package none.lwjgl.components.physic;

import com.google.inject.Inject;
import none.engine.Game;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import none.engine.component.physic.MasterPhysic;
import none.engine.component.physic.RigidBody;
import none.engine.scenes.Scene;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation from MasterPhysic class.
 */
public class MasterPhysicImpl extends MasterPhysic {
    public static final Logger LOGGER = LoggerFactory.getLogger(MasterPhysicImpl.class);
    private List<EngineObject> objects;

    @Inject
    public MasterPhysicImpl(Game game) {
        super(game);

        objects = new ArrayList<>();
    }

    @Override
    public void update(int deltaInMs, Scene currentScene) {
        objects.clear();
        iterateThroughScene(deltaInMs, currentScene.children(), currentScene);
    }

    private void iterateThroughScene(int deltaInMs, Iterable<EngineObject> children, EngineObject parent) {
        RigidBodyImpl rigidBody = null;
        TransformComponent transformComponent = null;

        //Find all Physic-Objects in scene.
        for (EngineObject child : children) {
            if (child.getName().equals(RigidBodyImpl.NAME)) {
                rigidBody = (RigidBodyImpl) child;
            } else if (child.getName().equals(TransformComponent.NAME)) {
                transformComponent = (TransformComponent) child;
            } else {
                iterateThroughScene(deltaInMs, child.children(), child);
            }
        }
        if (rigidBody != null && transformComponent != null) {
            objects.add(parent);
        }

        //Update Position, and check for collisions - handle correct.
        //This Algorithm should be replaced, Big-O(n^2).
        for (EngineObject object : objects) {
            TransformComponent objectTransform = (TransformComponent) object.find(TransformComponent.NAME).get();
            RigidBodyImpl objectBody = (RigidBodyImpl) object.find(RigidBodyImpl.NAME).get();

            //Only moveable objects can collide.
            if (objectBody.getType() != RigidBody.Type.Moveable) {
                continue;
            }

            boolean onFloor = false;
            Vector3d newPosition = update(deltaInMs, objectBody, objectTransform);

            if (objectBody.getVelocity().equals(new Vector3d())) {
                continue;
            }
            for (EngineObject other : objects) {
                TransformComponent otherTransform = (TransformComponent) other.find(TransformComponent.NAME).get();
                RigidBodyImpl otherBody = (RigidBodyImpl) other.find(RigidBodyImpl.NAME).get();

                if (!other.getId().equals(object.getId()) && collisionDetected(newPosition, objectBody, otherTransform, otherBody)) {
                    onFloor = true; //This is not entirely true.
                    Vector3d normal = new Vector3d(0, 1, 0); // And this is also not correct.

                    objectBody.getVelocity().reflect(normal);
                    objectBody.getVelocity().y = 0;//And here...

                    newPosition = new Vector3d(newPosition.x, objectTransform.getPosition().y, newPosition.z);
                    break;
                }
            }


            objectBody.setOnFloor(onFloor);
            objectTransform.getPosition().set(newPosition);
        }
    }

    private boolean collisionDetected(Vector3d transform, RigidBody body, TransformComponent otherTransform, RigidBody other) {
        Cube rec = new Cube(transform, body);
        Cube otherRec = new Cube(otherTransform.getPosition(), other);

        return rec.intersects(otherRec);
    }

    private Vector3d update(int deltaInMs, RigidBodyImpl rigidBody, TransformComponent transformComponent) {
        //Eulers-Method.
        Vector3d forceSum = new Vector3d(0, 0, 0);
        for (Vector3d force : rigidBody.getForces()) {
            forceSum.add(force);
        }

        Vector3d oldPosition = new Vector3d();
        oldPosition.set(transformComponent.getPosition());

        Vector3d acceleration = forceSum.div(rigidBody.getMass());
        rigidBody.getVelocity().add(acceleration.mul(deltaInMs));

        Vector3d velocity = new Vector3d();
        velocity.set(rigidBody.getVelocity());
        acceleration = oldPosition.add(velocity.mul(deltaInMs));

        return acceleration;
    }
}
