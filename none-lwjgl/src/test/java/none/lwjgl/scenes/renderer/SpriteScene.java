package none.lwjgl.scenes.renderer;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.Transform;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.Keyboard;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.Texture;
import none.engine.component.renderer.camera.Camera;
import none.engine.component.renderer.camera.OrthographicCamera;
import none.engine.component.renderer.primitives.Sprite;
import org.joml.Vector3d;

import java.util.List;

/**
 * To Test the Sprite.
 */
public class SpriteScene extends BaseScene {
    public static final String NAME = SpriteScene.class.getSimpleName();

    private final NextRow nextRow = new NextRow();
    private final NextColumn nextColumn = new NextColumn();

    private final UUIDFactory uuidFactory;
    private OrthographicCamera camera;

    private Texture texture;

    private SimpleSprite simpleSprite;
    private SimpleSprite controlSprite;

    private Keyboard keyboard;

    public SpriteScene(UUIDFactory uuidFactory, Game game, List<String> availableScenes) {
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
        camera.setFrustum(-range, range, -range, range, 0, range);

        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        texture = textureHandler.loadTexture("textures/texture.png");

        keyboard = getGame().getInjector().getInstance(Keyboard.class);
        keyboard.registerCommand(nextRow, Key.W);
        keyboard.registerCommand(nextColumn, Key.S);

        simpleSprite = new SimpleSprite(getGame());
        simpleSprite.init(range, 2, 4, new Vector3d());

        controlSprite = new SimpleSprite(getGame());
        controlSprite.init(range / 4, 1, 1, new Vector3d(range - range / 4, range - range / 4, 0));

        addObject(simpleSprite);
        addObject(controlSprite);
        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        if (keyboard.isCommandClicked(nextRow)) {
            simpleSprite.nextRow();
        }
        if (keyboard.isCommandClicked(nextColumn)) {
            simpleSprite.nextColumn();
        }

        super.update(deltaInMs);
    }

    @Override
    public void dispose() {
        keyboard.deregisterCommand(nextRow);
        keyboard.deregisterCommand(nextColumn);

        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);
        textureHandler.disposeTexture(texture);

        removeObject(simpleSprite);
        removeObject(controlSprite);

        super.dispose();
    }

    @Override
    protected String getInfoMessage() {
        return "Displays in the middle of the screen a single Sprite, at the right top corner" +
                "is the whole texture displayed. With the Key 'w' you can change the row, " +
                "press 's' to change the coloumn.";
    }

    public class SimpleSprite extends AbsStructObject<EngineObject> {
        private Sprite sprite;

        public SimpleSprite(Game game) {
            super(SimpleSprite.class.getSimpleName(), uuidFactory.createUUID(), game);
        }

        public void init(int size, int rowCount, int columnCount, Vector3d position) {
            Vector3d nullVector = new Vector3d();
            sprite = new Sprite(uuidFactory.createUUID(), rowCount, columnCount, size, size);
            Transform transform = new Transform(uuidFactory.createUUID(), getGame(), this, position, nullVector);

            addObject(new Renderable("Sprite", uuidFactory.createUUID(), sprite, texture, transform));
        }

        public void nextColumn() {
            int nextColumn = sprite.getColumn() + 1;
            if (nextColumn >= sprite.getMaxColumns()) {
                sprite.setColumn(0);
            } else {
                sprite.setColumn(nextColumn);
            }
        }

        public void nextRow() {
            int nextRow = sprite.getRow() + 1;
            if (nextRow >= sprite.getMaxRows()) {
                sprite.setRow(0);
            } else {
                sprite.setRow(nextRow);
            }
        }
    }

    private class NextRow implements Command {

    }

    private class NextColumn implements Command {

    }
}
