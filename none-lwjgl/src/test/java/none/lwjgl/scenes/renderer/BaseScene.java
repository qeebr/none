package none.lwjgl.scenes.renderer;

import none.engine.Game;
import none.engine.SceneManager;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.Keyboard;
import none.engine.scenes.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Base Scene, for all RendererScenes. Used to iterate through all known Scenes.
 */
public abstract class BaseScene extends AbsStructObject<EngineObject> implements Scene {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseScene.class);

    private static int currentSceneIndex;
    private final NextScene nextScene = new NextScene();
    private final PreviousScene previousScene = new PreviousScene();
    private final List<String> availableScenes;
    private Keyboard keyboard;
    private SceneManager sceneManager;

    protected BaseScene(String name, UUID id, Game game, List<String> availableScenes) {
        super(name, id, game);
        this.availableScenes = Objects.requireNonNull(availableScenes, "availableScenes");
    }

    @Override
    public void init() {
        LOGGER.info(this.getClass().getSimpleName() + ": " + getInfoMessage());
        keyboard = getGame().getInjector().getInstance(Keyboard.class);
        keyboard.registerCommand(nextScene, Key.RIGHT);
        keyboard.registerCommand(previousScene, Key.LEFT);

        sceneManager = getGame().getManager();

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        if (keyboard.isCommandReleased(nextScene)) {
            currentSceneIndex++;
            if (currentSceneIndex == availableScenes.size()) {
                currentSceneIndex = 0;
            }

            changeScene();
        } else if (keyboard.isCommandReleased(previousScene)) {
            currentSceneIndex--;
            if (currentSceneIndex < 0) {
                currentSceneIndex = availableScenes.size() - 1;
            }

            changeScene();
        }

        super.update(deltaInMs);
    }

    private void changeScene() {
        sceneManager.changeScene(availableScenes.get(currentSceneIndex));
    }

    @Override
    public void dispose() {
        keyboard.deregisterCommand(nextScene);
        keyboard.deregisterCommand(previousScene);

        super.dispose();
    }

    protected abstract String getInfoMessage();

    private class NextScene implements Command {

    }

    private class PreviousScene implements Command {

    }
}
