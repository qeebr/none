package none.goldminer.components.game;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.primitives.Text;
import none.goldminer.components.game.bricks.BrickFactory;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Keeps track of Level and difficulty.
 */
public class Level extends AbsStructObject<EngineObject> {
    public static final int INITIAL_POINT_THRESHOLD = 200;
    public static final int TIMER_DECREASE_MS = 200;
    public static final Logger LOGGER = LoggerFactory.getLogger(Level.class);

    private GameTicker gameTicker;
    private BrickFactory brickFactory;
    private Score score;

    private Renderable levelText;

    private int levelThreshold;
    private int level;

    public Level(UUID id, Game game) {
        super(Level.class.getSimpleName(), id, game);
    }

    public void init(GameTicker ticker, BrickFactory factory, Score score) {
        this.gameTicker = ticker;
        this.brickFactory = factory;
        this.score = score;

        Text text = new Text(UUID.randomUUID(), "1", 32);
        TransformComponent transform = new TransformComponent(UUID.randomUUID(), new Vector3d((32 * 4), 600 - (32 * 2), 0));
        this.levelText = new Renderable("Level-Text", UUID.randomUUID(), text, transform);

        this.levelThreshold = INITIAL_POINT_THRESHOLD;

        addObject(levelText);

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        super.update(deltaInMs);

        if (score.getScoreValue() >= levelThreshold) {
            LOGGER.info("Levelup");
            level++;
            levelText.getText().setText(String.valueOf(level));

            updateLevelThreshold();
            updateGameTicker();
            updateBrickFactory();
        }
    }

    private void updateGameTicker() {
        gameTicker.setTimerThreshold(gameTicker.getTimerThreshold() - TIMER_DECREASE_MS);
        LOGGER.info("GameTicker: {}", gameTicker.getTimerThreshold());
    }

    private void updateLevelThreshold() {
        levelThreshold += INITIAL_POINT_THRESHOLD;
        LOGGER.info("Threshold: {}", levelThreshold);
    }

    private void updateBrickFactory() {
        if (level % 10 == 0) {
            brickFactory.increaseLevel();
            LOGGER.info("Increased Brick Level");
        }
    }
}
