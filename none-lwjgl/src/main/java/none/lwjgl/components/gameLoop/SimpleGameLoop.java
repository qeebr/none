package none.lwjgl.components.gameLoop;

import none.engine.scenes.Scene;
import none.lwjgl.components.GameLoop;

/**
 * A Simple loop. Does update the Tree, Rendering and Sound.
 */
public class SimpleGameLoop extends GameLoop {

    @Override
    public void doLoop() {
        while (game.gameRunning()) {
            int delta = game.getDelta();

            //Update Inputs
            game.getKeyboard().update(delta);
            game.getMouse().update(delta);
            Scene currentScene = game.getManager().getCurrentScene();

            //Updates Tree
            game.getManager().update(delta);

            //Draw Tree
            game.getMasterRenderer().draw(currentScene);

            //Play Sounds
            game.getMasterPlayer().play(currentScene);

            //Update Screen.
            game.updateSceen();

            //Wait for next Frame.
            game.waitTimer();
        }
    }
}
