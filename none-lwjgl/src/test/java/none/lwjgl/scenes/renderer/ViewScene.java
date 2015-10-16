package none.lwjgl.scenes.renderer;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.CameraComponent;
import none.engine.component.renderer.OrthographicCamera;
import none.engine.component.renderer.Sprite;
import none.engine.component.renderer.Texture;
import org.joml.Vector3d;

import java.util.List;

/**
 * Tests the LookAt-Method.
 */
public class ViewScene extends BaseScene {
    public static final String NAME = ViewScene.class.getSimpleName();

    private final UUIDFactory uuidFactory;

    private OrthographicCamera camera;
    private Texture texture;
    private SimpleSprite bottomLeftSprite;
    private SimpleSprite topRightSprite;

    public ViewScene(UUIDFactory uuidFactory, Game game, List<String> availableScenes) {
        super(NAME, uuidFactory.createUUID(), game, availableScenes);
        this.uuidFactory = uuidFactory;
    }

    @Override
    public CameraComponent getActiveCamera() {
        return camera;
    }

    @Override
    public void init() {
        int range = 100;

        camera = new OrthographicCamera(uuidFactory.createUUID(), getGame());
        camera.setFrustum(-range, range, -range, range, -range, range);
        camera.lookAt(new Vector3d(range, range, 0), new Vector3d(range, range, -1), new Vector3d(0, 1, 0));

        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        texture = textureHandler.loadTexture("textures/texture.png");

        int spriteSize = 10;
        bottomLeftSprite = new SimpleSprite(getGame());
        bottomLeftSprite.init(spriteSize, new Vector3d(spriteSize / 2, spriteSize / 2, 0), new Vector3d());

        topRightSprite = new SimpleSprite(getGame());
        topRightSprite.init(spriteSize, new Vector3d((range * 2) - (spriteSize / 2), (range * 2) - (spriteSize / 2), 0), new Vector3d());

        addObject(bottomLeftSprite);
        addObject(topRightSprite);

        super.init();
    }

    @Override
    public void update(int delta) {
        super.update(delta);
    }

    @Override
    public void dispose() {
        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        textureHandler.disposeTexture(texture);

        removeObject(bottomLeftSprite);
        removeObject(topRightSprite);

        super.dispose();
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
