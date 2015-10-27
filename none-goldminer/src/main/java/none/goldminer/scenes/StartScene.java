package none.goldminer.scenes;

import none.engine.Game;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Text;
import none.goldminer.components.input.Confirm;
import none.goldminer.components.input.Reject;
import org.joml.Vector3d;

/**
 * The Scene displayed, after the game have been startet.
 */
public class StartScene extends BaseScene {
    public static final String NAME = StartScene.class.getSimpleName();

    private Text startGameText;

    public StartScene(UUIDFactory factory, Game game) {
        super(NAME, factory, game);
    }

    @Override
    public void init() {
        startGameText = new Text(uuidFactory.createUUID(), "Start Game", 64, new Vector3d(0, 0, 0));
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
