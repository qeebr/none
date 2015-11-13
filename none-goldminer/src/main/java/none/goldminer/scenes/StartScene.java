package none.goldminer.scenes;

import none.engine.Game;
import none.engine.component.Transform;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.primitives.Text;
import none.goldminer.components.input.Confirm;
import none.goldminer.components.input.Reject;
import org.joml.Vector3d;

/**
 * The Scene displayed, after the game have been startet.
 */
public class StartScene extends BaseScene {
    public static final String NAME = StartScene.class.getSimpleName();

    private Renderable startGameText;

    public StartScene(UUIDFactory factory, Game game) {
        super(NAME, factory, game);
    }

    @Override
    public void init() {
        Text text = new Text(uuidFactory.createUUID(), "Start Game", 64);
        Transform transform = new Transform(uuidFactory.createUUID(), new Vector3d(0, 0, 0));
        startGameText = new Renderable("StartScene-Greeter", uuidFactory.createUUID(), text, transform);
        addObject(startGameText);

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        if (keyboard.isCommandClicked(Confirm.COMMAND)) {
            getGame().getManager().changeScene(GameScene.NAME);
        } else if (keyboard.isCommandClicked(Reject.COMMAND)) {
            getGame().stop();
        }

        super.update(deltaInMs);
    }

    @Override
    public void dispose() {
        removeObject(startGameText);

        super.dispose();
    }
}
