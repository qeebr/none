package none.goldminer.components.game;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.KeyboardComponent;
import none.engine.component.renderer.Text;
import none.goldminer.components.game.cursor.Cursor;
import org.joml.Vector3d;

import java.util.UUID;

/**
 * Changes Color of a Brick.
 */
public class ColorChanger extends AbsStructObject<EngineObject> {
    public static final int TIMER_THRESHOLD = 5000;
    public static final String NAME = "ColorChanger";

    private final ChangeColor changeColor = new ChangeColor();

    private boolean ready = true;
    private int timer = 0;

    private Text statusText;

    private GameField gameField;
    private Cursor cursor;
    private KeyboardComponent keyboardComponent;

    public ColorChanger(UUID id, Game game) {
        super(NAME, id, game);
    }

    public void init(GameField gameField, Cursor cursor) {
        ready = true;
        timer = 0;

        statusText = new Text(UUID.randomUUID(), "Ready", 20, new Vector3d(700, 0, 0));

        this.gameField = gameField;
        this.cursor = cursor;

        keyboardComponent = getGame().getInjector().getInstance(KeyboardComponent.class);
        keyboardComponent.registerCommand(changeColor, Key.I_1);

        addObject(statusText);
    }

    @Override
    public void update(int delta) {
        if (ready) {
            if (keyboardComponent.isCommandClicked(changeColor)) {
                gameField.changeColor(cursor.getCurrentRow(), cursor.getCurrentColumn());

                ready = false;
                statusText.setText("not Ready");
            }
        } else {
            timer += delta;
            if (timer >= TIMER_THRESHOLD) {
                timer = 0;

                ready = true;
                statusText.setText("Ready");
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private class ChangeColor implements Command {

    }
}
