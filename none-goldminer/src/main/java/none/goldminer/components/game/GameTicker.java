package none.goldminer.components.game;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.component.EngineObject;
import none.goldminer.scenes.GameScene;

import java.util.UUID;

/**
 * A Ticker which calls the ticks method of the GameField.
 */
public class GameTicker extends AbsObject {
    public static final String NAME = "GameTicker";
    private final GameField gameField;

    private int currentTime;

    public GameTicker(UUID id, Game game, EngineObject parent, GameField gameField) {
        super(NAME, id, game, parent);
        this.gameField = gameField;
    }

    @Override
    public void update(int delta) {
        if (((GameScene) getParent()).getGameState() != GameState.RUNNING) {
            return;
        }

        currentTime += delta;

        if (currentTime >= 3000) {
            currentTime = 0;
            boolean gameOver = gameField.tick();
            if (gameOver) {
                ((GameScene) getParent()).setGameState(GameState.GAME_OVER);
            }
        }
    }
}
