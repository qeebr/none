package none.lwjgl.scenes.renderer;

import none.engine.Game;
import none.engine.SceneManager;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.KeyboardComponent;
import none.engine.scenes.Scene;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Base Scene, for all RendererScenes. Used to iterate through all known Scenes.
 */
public abstract class BaseScene extends AbsStructObject<EngineObject> implements Scene {

    private static int currentSceneIndex;
    private final NextScene nextScene = new NextScene();
    private final PreviousScene previousScene = new PreviousScene();
    private final List<String> availableScenes;
    private KeyboardComponent keyboardComponent;
    private SceneManager sceneManager;

    protected BaseScene(String name, UUID id, Game game, List<String> availableScenes) {
        super(name, id, game);

        this.availableScenes = Objects.requireNonNull(availableScenes, "availableScenes");
    }

    @Override
    public void init() {
        keyboardComponent = getGame().getInjector().getInstance(KeyboardComponent.class);
        keyboardComponent.registerCommand(nextScene, Key.RIGHT);
        keyboardComponent.registerCommand(previousScene, Key.LEFT);

        sceneManager = getGame().getManager();

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        if (keyboardComponent.isCommandReleased(nextScene)) {
            currentSceneIndex++;
            if (currentSceneIndex == availableScenes.size()) {
                currentSceneIndex = 0;
            }

            changeScene();
        } else if (keyboardComponent.isCommandReleased(previousScene)) {
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
        keyboardComponent.deregisterCommand(nextScene);
        keyboardComponent.deregisterCommand(previousScene);

        super.dispose();
    }

    private class NextScene implements Command {

    }

    private class PreviousScene implements Command {

    }
}
