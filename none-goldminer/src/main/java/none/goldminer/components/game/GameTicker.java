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
    public static final int INITIAL_TIME_SPAN = 3000;

    private final GameField gameField;

    private int timerThreshold;
    private int currentTime;

    public GameTicker(UUID id, Game game, EngineObject parent, GameField gameField) {
        super(NAME, id, game, parent);
        this.gameField = gameField;
    }

    public int getTimerThreshold() {
        return timerThreshold;
    }

    public void setTimerThreshold(int timerThreshold) {
        this.timerThreshold = timerThreshold;
    }

    @Override
    public void init() {
        super.init();
        timerThreshold = INITIAL_TIME_SPAN;
    }

    @Override
    public void update(int delta) {
        if (((GameScene) getParent()).getGameState() != GameState.RUNNING) {
            return;
        }

        currentTime += delta;

        if (currentTime >= timerThreshold) {
            currentTime = 0;
            boolean gameOver = gameField.tick();
            if (gameOver) {
                ((GameScene) getParent()).setGameState(GameState.GAME_OVER);
            }
        }
    }
}
