package none.lwjgl.components;

import none.lwjgl.LwjglGame;

import java.util.Objects;

/**
 * A abstraction for a GameLoop. Every game have different needs therefore
 * the GameLoop can be selfdefined for every Game.
 */
public abstract class GameLoop {
    protected LwjglGame game;

    public void setGame(LwjglGame game) {
        this.game = Objects.requireNonNull(game);
    }

    public abstract void doLoop();
}
