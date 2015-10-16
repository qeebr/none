package none.lwjgl.components.renderer;

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
import none.lwjgl.scenes.renderer.OrthograficScene;
import none.lwjgl.scenes.renderer.SpriteScene;
import none.lwjgl.scenes.renderer.TextScene;
import none.lwjgl.scenes.renderer.ViewScene;

import java.util.ArrayList;
import java.util.List;

/**
 * A Test Application for Renderer-part.
 */
public class RendererApp extends LwjglGame {

    public RendererApp(GameOptions options) {
        super(options);
    }

    public static void main(String[] args) {
        GameOptions options = createGameOptions();
        LwjglGame game = new LwjglGame(options);

        Injector injector = Guice.createInjector(new GameModule(game), new LwjglModule(new LwjglAssets()));
        UUIDFactory factory = injector.getInstance(UUIDFactory.class);

        List<String> sceneStringList = new ArrayList<>();
        sceneStringList.add(OrthograficScene.NAME);
        sceneStringList.add(SpriteScene.NAME);
        sceneStringList.add(ViewScene.NAME);
        sceneStringList.add(TextScene.NAME);

        OrthograficScene orthograficScene = new OrthograficScene(factory, game, sceneStringList);
        SpriteScene spriteScene = new SpriteScene(factory, game, sceneStringList);
        ViewScene viewScene = new ViewScene(factory, game, sceneStringList);
        TextScene textScene = new TextScene(factory, game, sceneStringList);

        List<Scene> sceneList = new ArrayList<>();
        sceneList.add(orthograficScene);
        sceneList.add(spriteScene);
        sceneList.add(viewScene);
        sceneList.add(textScene);

        SceneManager manager = new SceneManager(game, sceneList, TextScene.NAME);

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
