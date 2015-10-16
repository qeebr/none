package none.lwjgl.components.physic;

import none.engine.Game;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import none.engine.component.physic.Face;
import none.engine.component.physic.RigidBody;
import org.joml.Vector3d;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Should test MasterPhysics but tests cube.
 */
public class MasterPhysicImplTest {

    private RigidBodyImpl createRigidBody() {
        List<Vector3d> vertices = new ArrayList<>();
        vertices.add(new Vector3d(-1, -1, -1));
        vertices.add(new Vector3d(1, 1, 1));
        vertices.add(new Vector3d(1, -1, 1));

        List<Face> faces = new ArrayList<>();
        faces.add(new Face(vertices, new Vector3d(0, 1, 0)));

        return new RigidBodyImpl(UUID.randomUUID(), "foobar", RigidBody.Type.Moveable, faces);
    }

    private TransformComponent createTransform(Vector3d position) {
        Vector3d nullVector = new Vector3d();

        return new TransformComponent(UUID.randomUUID(),
                mock(Game.class), mock(EngineObject.class), position, nullVector);
    }

    @Test
    public void testCubeIntersection() {
        RigidBodyImpl rigidBody = createRigidBody();

        Cube cube = new Cube(createTransform(new Vector3d(0, 3, 0)).getPosition(), rigidBody);
        Cube other = new Cube(createTransform(new Vector3d(0, 0, 0)).getPosition(), rigidBody);

        assertFalse(cube.intersects(other));

        cube = new Cube(createTransform(new Vector3d(0, 1.5, 0)).getPosition(), rigidBody);

        assertTrue(cube.intersects(other));
    }
}