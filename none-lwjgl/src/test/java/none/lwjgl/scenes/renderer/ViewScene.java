package none.lwjgl.scenes.renderer;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.Transform;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.Texture;
import none.engine.component.renderer.camera.Camera;
import none.engine.component.renderer.camera.OrthographicCamera;
import none.engine.component.renderer.primitives.Sprite;
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
    public Camera getActiveCamera() {
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
    public void update(int deltaInMs) {
        super.update(deltaInMs);
    }

    @Override
    public void dispose() {
        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        textureHandler.disposeTexture(texture);

        removeObject(bottomLeftSprite);
        removeObject(topRightSprite);

        super.dispose();
    }

    @Override
    protected String getInfoMessage() {
        return "Tests the lookAt-Implementation. At the bottom left corner" +
                "and the top right corner a sprite should be visible.";
    }

    public class SimpleSprite extends AbsStructObject<EngineObject> {
        private Sprite sprite;
        private Transform transform;

        public SimpleSprite(Game game) {
            super(SimpleSprite.class.getSimpleName(), uuidFactory.createUUID(), game);
        }

        public void init(int size, Vector3d position, Vector3d direction) {
            this.sprite = new Sprite(uuidFactory.createUUID(), 2, 4, size, size);
            this.transform = new Transform(uuidFactory.createUUID(), getGame(), this, position, direction);

            addObject(new Renderable("Sprite", uuidFactory.createUUID(), sprite, texture, transform));

            super.init();
        }
    }
}
