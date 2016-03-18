package none.lwjgl.scenes.ui;

import none.engine.Game;
import none.engine.GameOptions;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Texture;
import none.engine.component.renderer.camera.Camera;
import none.engine.component.renderer.camera.OrthographicCamera;
import none.engine.component.ui.Button;
import none.engine.component.ui.Label;
import none.engine.component.ui.Textbox;
import none.engine.component.ui.Window;
import none.engine.component.ui.factories.UiFactory;
import none.engine.scenes.BaseScene;

/**
 * Test-Scene for Ui.
 */
public class UiScene extends BaseScene {
    public static final String NAME = UiScene.class.getSimpleName();

    private UUIDFactory uuidFactory;
    private Camera camera;
    private Texture windowTexture;
    private Texture textboxTexture;
    private Texture downButton;
    private Texture upButton;

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

        UiFactory uiFactory = getGame().getInjector().getInstance(UiFactory.class);
        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        this.windowTexture = textureHandler.loadTexture("textures/window.png");
        Window window = uiFactory.buildWindow("MainWindow", windowTexture).with(300, 400, 200, 200);
        addObject(window);

        this.textboxTexture = textureHandler.loadTexture("textures/textbox.png");
        Textbox textbox = uiFactory.buildTextbox("txtUserInput", textboxTexture).with(325, 375, 150, 50);
        window.addObject(textbox);

        Label label = uiFactory.builLabel("label", "").with(325, 250, 150, 50);
        window.addObject(label);

        this.downButton = textureHandler.loadTexture("textures/downButton.png");
        this.upButton = textureHandler.loadTexture("textures/upButton.png");
        Button button = uiFactory.buildButton("btnAction", "Hello", upButton, downButton).with(325, 300, 150, 50);
        window.addObject(button);

        button.registerButtonHandler(() -> label.getTextContent().setText(textbox.getTextContent().getText()));

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        super.update(deltaInMs);
    }

    @Override
    public void dispose() {
        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        textureHandler.disposeTexture(windowTexture);
        textureHandler.disposeTexture(textboxTexture);
        textureHandler.disposeTexture(downButton);
        textureHandler.disposeTexture(upButton);
        windowTexture = null;

        super.dispose();
    }
}
