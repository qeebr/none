package none.lwjgl.apps.input;

import com.google.inject.Guice;
import com.google.inject.Injector;
import none.engine.GameModule;
import none.engine.GameOptions;
import none.engine.SceneManager;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.scenes.Scene;
import none.lwjgl.LwjglGame;
import none.lwjgl.LwjglModule;
import none.lwjgl.components.assets.LwjglAssets;
import none.lwjgl.components.gameLoop.SimpleGameLoop;
import none.lwjgl.scenes.input.MouseScene;

import java.util.ArrayList;
import java.util.List;

/**
 * TestApp for input.
 */
public class InputApp {

    public static void main(String[] args) {
        GameOptions options = createGameOptions();
        LwjglGame game = new LwjglGame(options, new SimpleGameLoop());

        Injector injector = Guice.createInjector(new GameModule(game), new LwjglModule(new LwjglAssets()));
        UUIDFactory factory = injector.getInstance(UUIDFactory.class);

        MouseScene mouseScene = new MouseScene(factory, game);

        List<Scene> sceneList = new ArrayList<>();
        sceneList.add(mouseScene);

        SceneManager manager = new SceneManager(game, sceneList, MouseScene.NAME);

        game.run(manager, injector);
    }

    private static GameOptions createGameOptions() {
        GameOptions options = new GameOptions();

        options.setDisplayWidth(800);
        options.setDisplayHeight(600);
        options.setFarPlane(100);
        options.setNearPlane(0.1f);
        options.setFieldOfView(90);

        return options;
    }
}
