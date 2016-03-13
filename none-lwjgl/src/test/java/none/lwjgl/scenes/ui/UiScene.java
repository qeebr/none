package none.lwjgl.scenes.ui;

import none.engine.Game;
import none.engine.GameOptions;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Texture;
import none.engine.component.renderer.camera.Camera;
import none.engine.component.renderer.camera.OrthographicCamera;
import none.engine.component.ui.TexturePart;
import none.engine.component.ui.UiTexture;
import none.engine.component.ui.Window;
import none.engine.scenes.BaseScene;

/**
 * Test-Scene for Ui.
 */
public class UiScene extends BaseScene {
    public static final String NAME = UiScene.class.getSimpleName();

    private UUIDFactory uuidFactory;
    private Camera camera;
    private Texture windowTexture;

    public UiScene(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);
        this.uuidFactory = factory;
    }

    @Override
    public Camera getActiveCamera() {
        return camera;
    }

    @Override
    public void init() {
        GameOptions options = getGame().getOptions();
        OrthographicCamera camera = new OrthographicCamera(uuidFactory.createUUID(), getGame());
        camera.setFrustum(0, options.getDisplayWidth(), 0, options.getDisplayHeight(), -100, 100);
        this.camera = camera;

        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        this.windowTexture = textureHandler.loadTexture("textures/window.png");
        UiTexture uiTexture = initUiTexture(windowTexture);
        Window window = initWindow(uiTexture);
        addObject(window);

        super.init();
    }

    private UiTexture initUiTexture(Texture texture) {
        double third = texture.getWidth() / 3;

        UiTexture uiTexture = new UiTexture(texture);
        uiTexture.setVertices(UiTexture.UPPER_LEFT_CORNER, new TexturePart(0.0, third, third, third));
        uiTexture.setVertices(UiTexture.UPPER_MIDDLE, new TexturePart(third, third, third, third));
        uiTexture.setVertices(UiTexture.UPPER_RIGHT_CORNER, new TexturePart(texture.getWidth() - third, third, third, third));

        uiTexture.setVertices(UiTexture.RIGHT_MIDDLE, new TexturePart(texture.getWidth() - third, texture.getHeight() - third, third, third));

        uiTexture.setVertices(UiTexture.LOWER_RIGHT_CORNER, new TexturePart(texture.getWidth() - third, texture.getHeight(), third, third));
        uiTexture.setVertices(UiTexture.LOWER_MIDDLE, new TexturePart(third, texture.getHeight(), third, third));
        uiTexture.setVertices(UiTexture.LOWER_LEFT_CORNER, new TexturePart(0.0, texture.getHeight(), third, third));

        uiTexture.setVertices(UiTexture.LEFT_MIDDLE, new TexturePart(0.0, texture.getHeight() - third, third, third));

        uiTexture.setVertices(UiTexture.MIDDLE, new TexturePart(third, texture.getHeight() - third, third, third));

        return uiTexture;
    }

    private Window initWindow(UiTexture uiTexture) {
        Window window = new Window(uuidFactory.createUUID(), getGame(), uiTexture);
        window.setX(300);
        window.setY(400);
        window.setHeight(200);
        window.setWidth(200);

        return window;
    }

    @Override
    public void update(int deltaInMs) {
        super.update(deltaInMs);
    }

    @Override
    public void dispose() {
        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        textureHandler.disposeTexture(windowTexture);
        windowTexture = null;

        super.dispose();
    }
}
