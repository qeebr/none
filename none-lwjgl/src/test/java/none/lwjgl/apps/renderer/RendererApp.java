package none.lwjgl.apps.renderer;

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
import none.lwjgl.scenes.renderer.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A Test Application for Renderer-part.
 */
public class RendererApp {

    public static void main(String[] args) {
        GameOptions options = createGameOptions();
        LwjglGame game = new LwjglGame(options, new SimpleGameLoop());

        Injector injector = Guice.createInjector(new GameModule(game), new LwjglModule(new LwjglAssets()));
        UUIDFactory factory = injector.getInstance(UUIDFactory.class);

        List<String> sceneStringList = new ArrayList<>();
        sceneStringList.add(OrthograficScene.NAME);
        sceneStringList.add(SpriteScene.NAME);
        sceneStringList.add(ViewScene.NAME);
        sceneStringList.add(TextScene.NAME);
        sceneStringList.add(MeshScene.NAME);

        //Check Main-Arguments, for Start-Scene.
        String startScene = TextScene.NAME;
        if (args.length == 1 && sceneStringList.contains(args[0])) {
            startScene = args[0];
        }

        OrthograficScene orthograficScene = new OrthograficScene(factory, game, sceneStringList);
        SpriteScene spriteScene = new SpriteScene(factory, game, sceneStringList);
        ViewScene viewScene = new ViewScene(factory, game, sceneStringList);
        TextScene textScene = new TextScene(factory, game, sceneStringList);
        MeshScene meshScene = new MeshScene(factory, game, sceneStringList);

        List<Scene> sceneList = new ArrayList<>();
        sceneList.add(orthograficScene);
        sceneList.add(spriteScene);
        sceneList.add(viewScene);
        sceneList.add(textScene);
        sceneList.add(meshScene);

        SceneManager manager = new SceneManager(game, sceneList, startScene);

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
