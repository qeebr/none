package none.goldminer.components.messages;

import none.engine.component.messages.Message;
import none.goldminer.components.game.GameState;

/**
 * Message when GameState has changed.
 */
public class GameStateChanged implements Message {
    private GameState newGameState;
    private GameState oldGameState;

    public GameStateChanged(GameState newGameState, GameState oldGameState) {
        this.newGameState = newGameState;
        this.oldGameState = oldGameState;
    }

    public GameState getNewGameState() {
        return newGameState;
    }

    public GameState getOldGameState() {
        return oldGameState;
    }
}
