package none.goldminer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import none.engine.Game;
import none.engine.GameModule;
import none.engine.GameOptions;
import none.engine.SceneManager;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.scenes.Scene;
import none.goldminer.components.assets.GameAssets;
import none.goldminer.scenes.GameScene;
import none.goldminer.scenes.StartScene;
import none.lwjgl.LwjglGame;
import none.lwjgl.LwjglModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The GoldMiner GameApp.
 */
public class GameApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameApp.class);

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(GameApp.class.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            LOGGER.error("Exception during Config loading.", e);
            return;
        }
        GameOptions options = createOptions(properties);

        Game game = new LwjglGame(options);

        Injector injector = Guice.createInjector(new GameModule(game), new LwjglModule(new GameAssets()));
        UUIDFactory factory = injector.getInstance(UUIDFactory.class);

        GameScene gameScene = new GameScene(factory, game);
        StartScene startScene = new StartScene(factory, game);

        List<Scene> sceneList = new ArrayList<>();
        sceneList.add(gameScene);
        sceneList.add(startScene);

        SceneManager manager = new SceneManager(game, sceneList, startScene.getName());

        game.run(manager, injector);
    }

    private static GameOptions createOptions(Properties properties) {
        GameOptions options = new GameOptions();

        String value = properties.getProperty("DisplayHeight");
        options.setDisplayHeight(Integer.valueOf(value));

        value = properties.getProperty("DisplayWidth");
        options.setDisplayWidth(Integer.valueOf(value));

        value = properties.getProperty("NearPlane");
        options.setNearPlane(Float.valueOf(value));

        value = properties.getProperty("FarPlane");
        options.setFarPlane(Float.valueOf(value));

        value = properties.getProperty("FieldOfView");
        options.setFieldOfView(Integer.valueOf(value));

        return options;
    }
}
