package none.lwjgl.scenes.renderer;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import none.engine.component.assets.MeshHandler;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.*;
import none.engine.scenes.Scene;
import org.joml.Vector3d;

import java.util.List;

/**
 * A Scene to Test the Renderer.
 */
public class OrthograficScene extends BaseScene implements Scene {
    public static final String NAME = OrthograficScene.class.getSimpleName();

    private final UUIDFactory uuidFactory;
    private OrthographicCamera camera;

    private Texture texture;
    private Mesh mesh;

    private SimpleSprite centerSprite;
    private SimpleSprite bottomLeftSprite;
    private SimpleSprite bottomRightSprite;
    private SimpleSprite topRightSprite;
    private SimpleSprite topLeftSprite;

    private SimpleMesh frontMesh;
    private SimpleMesh backMesh;

    public OrthograficScene(UUIDFactory factory, Game game, List<String> availableScenes) {
        super(NAME, factory.createUUID(), game, availableScenes);

        this.uuidFactory = factory;
    }

    @Override
    public CameraComponent getActiveCamera() {
        return camera;
    }

    @Override
    public void init() {
        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        MeshHandler meshHandler = getGame().getInjector().getInstance(MeshHandler.class);

        double range = 10;
        int size = 2;

        camera = new OrthographicCamera(uuidFactory.createUUID(), getGame());
        camera.setFrustum(-range, range, -range, range, -range, range);

        texture = textureHandler.loadTexture("textures/texture.png");
        mesh = meshHandler.loadMesh("models/simpleCube.obj");
        Vector3d noDirection = new Vector3d();

        centerSprite = new SimpleSprite(getGame());
        centerSprite.init(size, new Vector3d(0, 0, 0), noDirection);

        bottomLeftSprite = new SimpleSprite(getGame());
        bottomLeftSprite.init(size, new Vector3d(-range + (size / 2), -range + (size / 2), 0), noDirection);

        bottomRightSprite = new SimpleSprite(getGame());
        bottomRightSprite.init(size, new Vector3d(range - (size / 2), -range + (size / 2), 0), noDirection);

        topRightSprite = new SimpleSprite(getGame());
        topRightSprite.init(size, new Vector3d(range - (size / 2), range - (size / 2), 0), noDirection);

        topLeftSprite = new SimpleSprite(getGame());
        topLeftSprite.init(size, new Vector3d(-range + (size / 2), range - (size / 2), 0), noDirection);

        backMesh = new SimpleMesh(getGame());
        backMesh.init(new Vector3d(-1.2, -5, -(range / 2)), noDirection);

        frontMesh = new SimpleMesh(getGame());
        frontMesh.init(new Vector3d(1.2, -5, (range / 2)), noDirection);


        addObject(centerSprite);
        addObject(bottomLeftSprite);
        addObject(bottomRightSprite);
        addObject(topRightSprite);
        addObject(topLeftSprite);

        addObject(backMesh);
        addObject(frontMesh);

        super.init();
    }

    @Override
    public void update(int delta) {
        super.update(delta);
    }

    @Override
    public void dispose() {
        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        MeshHandler meshHandler = getGame().getInjector().getInstance(MeshHandler.class);

        textureHandler.disposeTexture(texture);
        meshHandler.disposeMesh(mesh);

        removeObject(centerSprite);
        removeObject(bottomLeftSprite);
        removeObject(bottomRightSprite);
        removeObject(topRightSprite);
        removeObject(topLeftSprite);

        removeObject(backMesh);
        removeObject(frontMesh);

        super.dispose();
    }

    public class SimpleMesh extends AbsStructObject<EngineObject> {
        private TransformComponent transformComponent;

        public SimpleMesh(Game game) {
            super(SimpleMesh.class.getSimpleName(), uuidFactory.createUUID(), game);
        }

        public void init(Vector3d position, Vector3d direction) {
            this.transformComponent = new TransformComponent(uuidFactory.createUUID(), getGame(), this, position, direction);

            addObject(texture);
            addObject(mesh);
            addObject(transformComponent);

            super.init();
        }

    }

    public class SimpleSprite extends AbsStructObject<EngineObject> {
        private Sprite sprite;
        private TransformComponent transformComponent;

        public SimpleSprite(Game game) {
            super(SimpleSprite.class.getSimpleName(), uuidFactory.createUUID(), game);
        }

        public void init(int size, Vector3d position, Vector3d direction) {
            this.sprite = new Sprite(uuidFactory.createUUID(), 2, 4, size, size);
            this.transformComponent = new TransformComponent(uuidFactory.createUUID(), getGame(), this, position, direction);

            addObject(texture);
            addObject(sprite);
            addObject(transformComponent);

            super.init();
        }
    }
}
