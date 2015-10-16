package none.engine.component.physic;

import none.engine.Game;
import none.engine.scenes.Scene;

import java.util.Objects;

/**
 * Handles Physic-Objects in a Scene.
 */
public abstract class MasterPhysic {
    private Game game;

    public MasterPhysic(Game game) {
        this.game = Objects.requireNonNull(game);
    }

    public Game getGame() {
        return game;
    }

    public abstract void update(int deltaInMs, Scene currentScene);

}
