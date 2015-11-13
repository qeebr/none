package none.goldminer.components.game;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.Transform;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.Keyboard;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.primitives.Text;
import none.goldminer.scenes.GameScene;
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

    private Renderable statusText;

    private Keyboard keyboard;
    private GameScene gameScene;

    public ColorChanger(UUID id, Game game, EngineObject parent) {
        super(NAME, id, game, parent);
    }

    @Override
    public void init() {
        ready = true;
        timer = 0;

        Text text = new Text(UUID.randomUUID(), "Ready", 20);
        Transform transform = new Transform(UUID.randomUUID(), new Vector3d(700, 0, 0));

        statusText = new Renderable("ColorChanger-Readytext", UUID.randomUUID(), text, transform);
        gameScene = (GameScene) getParent();

        keyboard = getGame().getInjector().getInstance(Keyboard.class);
        keyboard.registerCommand(changeColor, Key.I_1);

        addObject(statusText);

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        if (ready) {
            if (keyboard.isCommandClicked(changeColor)) {
                boolean changeColor = gameScene.changeColor();

                if (changeColor) {
                    ready = false;
                    statusText.getText().setText("not Ready");
                }
            }
        } else {
            timer += deltaInMs;
            if (timer >= TIMER_THRESHOLD) {
                timer = 0;

                ready = true;
                statusText.getText().setText("Ready");
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
