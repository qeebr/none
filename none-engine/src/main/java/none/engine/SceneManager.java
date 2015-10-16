package none.engine;

import com.google.common.base.Preconditions;
import none.engine.component.AbsObject;
import none.engine.scenes.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all Scenes in game.
 */
public class SceneManager extends AbsObject {
    public static final String NAME = "SceneManager";
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneManager.class);

    private final Map<String, Scene> sceneMap;

    private Scene currentScene;
    private Scene nextScene;

    public SceneManager(Game game, List<Scene> sceneList, String firstScene) {
        super(NAME, UUID.randomUUID(), game);

        sceneMap = new HashMap<>();
        nextScene = null;

        for (Scene scene : sceneList) {
            sceneMap.put(scene.getName(), scene);

            if (scene.getName().equals(firstScene)) {
                currentScene = scene;
            }
        }
        Preconditions.checkNotNull(currentScene, "currentScene/firstScene");
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void changeScene(String nextScene) {
        this.nextScene = sceneMap.get(nextScene);
    }

    @Override
    public void init() {
        currentScene.init();
    }

    @Override
    public void update(int delta) {
        if (nextScene != null) {
            LOGGER.info("Scene change. Dispose old scene {}", currentScene.getName());
            currentScene.dispose();
            LOGGER.info("Scene change. Initialize new scene {}", nextScene.getName());
            nextScene.init();
            currentScene = nextScene;
            nextScene = null;
            LOGGER.info("Scene change -> Done.");
        }

        currentScene.update(delta);
    }

    @Override
    public void dispose() {
        currentScene.dispose();
    }

    public <T extends Scene> T getScene(String name) {
        for (Map.Entry<String, Scene> entry : sceneMap.entrySet()) {
            if (entry.getKey().equals(name)) {
                return (T) entry.getValue();
            }
        }
        throw new IllegalStateException("Unknown Scene.");
    }
}
