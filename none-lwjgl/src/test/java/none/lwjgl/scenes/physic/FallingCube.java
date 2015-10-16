package none.lwjgl.scenes.physic;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import none.engine.component.assets.MeshHandler;
import none.engine.component.assets.PhysicHandler;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.KeyboardComponent;
import none.engine.component.physic.RigidBody;
import none.engine.component.renderer.CameraComponent;
import none.engine.component.renderer.Mesh;
import none.engine.component.renderer.PerspectiveCamera;
import none.engine.component.renderer.Texture;
import none.engine.scenes.Scene;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A cube falls down, and also can jump.
 */
public class FallingCube extends AbsStructObject<EngineObject> implements Scene {
    public static final String NAME = FallingCube.class.getSimpleName();

    private final Jump JUMP = new Jump();
    private final MoveForward MOVE_FORWARD = new MoveForward();
    private final MoveBackward MOVE_BACKWARD = new MoveBackward();
    private final UUIDFactory uuidFactory;

    private PerspectiveCamera camera;

    private Mesh cube;
    private Texture texture;
    private RigidBody levelBody;
    private RigidBody cubeBody;

    private Level level;
    private SimpleObject fallingCube;
    private KeyboardComponent keyboard;
    private Mesh plane;

    public FallingCube(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);

        uuidFactory = Objects.requireNonNull(factory, "factory");
    }

    @Override
    public CameraComponent getActiveCamera() {
        return camera;
    }

    @Override
    public void init() {
        camera = new PerspectiveCamera(uuidFactory.createUUID(), getGame());
        camera.init();
        camera.lookAt(new Vector3d(0, 2, 5), new Vector3d(0, 2, 0), new Vector3d(0, 1, 0));

        MeshHandler meshHandler = getGame().getInjector().getInstance(MeshHandler.class);
        cube = meshHandler.loadMesh("models/simpleCube.obj");
        plane = meshHandler.loadMesh("models/plane.obj");

        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        texture = textureHandler.loadTexture("textures/texture.png");

        PhysicHandler physicHandler = getGame().getInjector().getInstance(PhysicHandler.class);

        levelBody = physicHandler.loadFloorRigidBody("models/plane.obj");

        cubeBody = physicHandler.loadMoveableRigidBody("models/simpleCube.obj");
        cubeBody.getForces().add(new Vector3d(0, -0.000001, 0));

        level = new Level(getGame());
        addObject(level);

        fallingCube = new SimpleObject("FallingCube", getGame());
        fallingCube.init(new Vector3d(0, 5, 0), cubeBody, cube);
        addObject(fallingCube);

        keyboard = getGame().getInjector().getInstance(KeyboardComponent.class);
        keyboard.registerCommand(JUMP, Key.SPACE);
        keyboard.registerCommand(MOVE_FORWARD, Key.W);
        keyboard.registerCommand(MOVE_BACKWARD, Key.S);

        super.init();
    }

    @Override
    public void update(int delta) {

        if (cubeBody.isOnFloor() && keyboard.isCommandClicked(JUMP)) {
            cubeBody.setVelocity(cubeBody.getVelocity().add(new Vector3d(0, 0.001, 0)));
        }

        double speed = 0.0007;

        if (keyboard.isCommandClicked(MOVE_FORWARD)) {
            cubeBody.setVelocity(cubeBody.getVelocity().add(new Vector3d(0, 0, -speed)));
        } else if (keyboard.isCommandReleased(MOVE_FORWARD)) {
            cubeBody.setVelocity(cubeBody.getVelocity().add(new Vector3d(0, 0, speed)));
        }

        if (keyboard.isCommandClicked(MOVE_BACKWARD)) {
            cubeBody.setVelocity(cubeBody.getVelocity().add(new Vector3d(0, 0, speed)));
        } else if (keyboard.isCommandReleased(MOVE_BACKWARD)) {
            cubeBody.setVelocity(cubeBody.getVelocity().add(new Vector3d(0, 0, -speed)));
        }

        /*if (keyboard.isCommandClicked(MOVE_FORWARD) || keyboard.isCommandReleased(MOVE_BACKWARD)) {
            if (cubeBody.getVelocity().getZ() >= -speed) {
                if (cubeBody.isOnFloor()) {
                    cubeBody.setVelocity(new Vector3d(cubeBody.getVelocity().getX(), cubeBody.getVelocity().getY(), -speed));
                } else if (cubeBody.getVelocity().getZ() >= -speed) {
                    cubeBody.setVelocity(cubeBody.getVelocity().add(new Vector3d(0, 0, -speed)));
                }
            }

        }
        if (keyboard.isCommandClicked(MOVE_BACKWARD) || keyboard.isCommandReleased(MOVE_FORWARD)) {
            if (cubeBody.getVelocity().getZ() <= speed) {
                if (cubeBody.isOnFloor()) {
                    cubeBody.setVelocity(new Vector3d(cubeBody.getVelocity().getX(), cubeBody.getVelocity().getY(), speed));
                } else {
                    cubeBody.setVelocity(cubeBody.getVelocity().add(new Vector3d(0, 0, speed)));
                }
            }
        }*/

        super.update(delta);
    }

    private class Level extends AbsStructObject<EngineObject> {
        private List<SimpleObject> floor;

        public Level(Game game) {
            super(Level.class.getSimpleName(), uuidFactory.createUUID(), game);

            floor = new ArrayList<>();
        }

        @Override
        public void init() {
            SimpleObject cube = new SimpleObject("LevelBrick", getGame());
            cube.init(new Vector3d(0, 0, 0), levelBody, plane);
            floor.add(cube);

            addObject(cube);

            super.init();
        }
    }

    private class SimpleObject extends AbsStructObject<EngineObject> {
        private TransformComponent transformComponent;

        public SimpleObject(String name, Game game) {
            super(name, uuidFactory.createUUID(), game);
        }

        public void init(Vector3d position, RigidBody body, Mesh mesh) {
            transformComponent = new TransformComponent(uuidFactory.createUUID(), getGame(), this, position, new Vector3d());

            addObject(mesh);
            addObject(texture);
            addObject(body);
            addObject(transformComponent);

            super.init();
        }
    }

    private class Jump implements Command {

    }

    private class MoveForward implements Command {

    }

    private class MoveBackward implements Command {

    }

}
