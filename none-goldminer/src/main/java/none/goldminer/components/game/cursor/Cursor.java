package none.goldminer.components.game.cursor;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.KeyboardComponent;
import none.engine.component.renderer.Sprite;
import none.engine.component.renderer.Texture;
import none.goldminer.components.game.GameField;
import none.goldminer.components.game.bricks.Brick;
import none.goldminer.scenes.GameScene;
import org.joml.Vector3d;

import java.util.UUID;

/**
 * The Player-Cursor.
 */
public class Cursor extends AbsStructObject<EngineObject> {
    public static final String NAME = "Cursor";
    public static final int CURSOR_SIZE = 60;
    public static final int SPRITE_LAYER = 1;

    private UpCommand upCommand = new UpCommand();
    private DownCommand downCommand = new DownCommand();
    private LeftCommand leftCommand = new LeftCommand();
    private RightCommand rightCommand = new RightCommand();
    private ActionCommand actionCommand = new ActionCommand();

    private TextureHandler textureHandler;

    private Texture texture;
    private Sprite sprite;
    private TransformComponent transform;
    private CursorAnimator cursorAnimator;

    private int currentRow;
    private int currentColumn;

    private KeyboardComponent keyboard;

    public Cursor(UUID id, Game game, EngineObject parent) {
        super(NAME, id, game, parent);

        currentRow = 0;
        currentColumn = 0;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    @Override
    public void init() {
        UUIDFactory uuidFactory = getGame().getInjector().getInstance(UUIDFactory.class);
        textureHandler = getGame().getInjector().getInstance(TextureHandler.class);

        texture = textureHandler.loadTexture("textures/cursor.png");
        sprite = new Sprite(uuidFactory.createUUID(), 1, 3, CURSOR_SIZE, CURSOR_SIZE, SPRITE_LAYER);
        transform = new TransformComponent(uuidFactory.createUUID(), getGame(), this, calculateCurrentPosition(), new Vector3d());
        cursorAnimator = new CursorAnimator(uuidFactory.createUUID(), sprite);

        addObject(texture);
        addObject(sprite);
        addObject(transform);
        addObject(cursorAnimator);

        keyboard = getGame().getInjector().getInstance(KeyboardComponent.class);
        keyboard.registerCommand(downCommand, Key.DOWN);
        keyboard.registerCommand(upCommand, Key.UP);
        keyboard.registerCommand(leftCommand, Key.LEFT);
        keyboard.registerCommand(rightCommand, Key.RIGHT);

        keyboard.registerCommand(actionCommand, Key.SPACE);

        super.init();
    }

    @Override
    public void update(int deltaInMs) {

        if (keyboard.isCommandClicked(downCommand) && currentRow > 0) {
            currentRow--;
        } else if (keyboard.isCommandClicked(upCommand) && currentRow < (GameField.MAX_ROWS - 1)) {
            currentRow++;
        }
        if (keyboard.isCommandClicked(leftCommand) && currentColumn > 0) {
            currentColumn--;
        } else if (keyboard.isCommandClicked(rightCommand) && currentColumn < (GameField.MAX_COLUMNS - 1)) {
            currentColumn++;
        }
        ((TransformComponent) find(TransformComponent.NAME).get()).getPosition().set(calculateCurrentPosition());

        if (keyboard.isCommandClicked(actionCommand)) {
            cursorAnimator.doAnimation();
            ((GameScene) getParent()).removeBrick(currentRow, currentColumn);
        }

        super.update(deltaInMs);
    }

    @Override
    public void dispose() {
        textureHandler.disposeTexture(texture);

        super.dispose();
    }

    private Vector3d calculateCurrentPosition() {
        return new Vector3d((GameField.FIELD_X + (Brick.BRICK_SIZE * currentColumn)) + (CURSOR_SIZE / 1.5),
                (GameField.FIELD_Y + (Brick.BRICK_SIZE * currentRow)) - (CURSOR_SIZE / 1.5), 1);
    }

    private class UpCommand implements Command {

    }

    private class DownCommand implements Command {

    }

    private class RightCommand implements Command {

    }

    private class LeftCommand implements Command {

    }

    private class ActionCommand implements Command {

    }
}
