package none.goldminer.scenes;

import none.engine.Game;
import none.engine.component.EngineObject;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Text;
import none.goldminer.components.game.*;
import none.goldminer.components.game.bricks.Brick;
import none.goldminer.components.game.bricks.BrickFactory;
import none.goldminer.components.game.bricks.BrickMoveAnimation;
import none.goldminer.components.game.cursor.Cursor;
import none.goldminer.components.input.Confirm;
import none.goldminer.components.input.Reject;
import none.goldminer.components.messages.GameStateChanged;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

/**
 * The GoldMine GameScene.
 */
public class GameScene extends BaseScene {
    public static final String NAME = "GameScene";
    public static final String HIGH_SCORE_FILE = "highscore.properties";
    public static final Logger LOGGER = LoggerFactory.getLogger(GameScene.class);

    private Text gameOverText;
    private Text newHighscore;

    private GameState gameState;
    private HighScore highScore;
    private Score score;
    private GameField gameField;
    private Cursor cursor;

    public GameScene(UUIDFactory factory, Game game) {
        super(NAME, factory, game);

        this.highScore = new HighScore(GameScene.class.getResourceAsStream(HIGH_SCORE_FILE));
        this.gameState = GameState.GAME_OVER;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        getGame().getMessageBus().sendMessage(new GameStateChanged(gameState, this.gameState));

        this.gameState = gameState;
    }

    @Override
    public void init() {
        gameState = GameState.RUNNING;

        BrickFactory brickFactory = new BrickFactory(uuidFactory.createUUID(), getGame(), this);
        gameField = new GameField(uuidFactory.createUUID(), getGame(), brickFactory);
        cursor = new Cursor(uuidFactory.createUUID(), getGame(), this);
        GameTicker ticker = new GameTicker(uuidFactory.createUUID(), getGame(), this, gameField);
        score = new Score(getGame(), this);
        ColorChanger colorChanger = new ColorChanger(uuidFactory.createUUID(), getGame(), this);
        gameOverText = new Text(uuidFactory.createUUID(), "Game Over :(", 32, new Vector3d(400 - (32 * 6), 300, 0));
        newHighscore = new Text(uuidFactory.createUUID(), "NEW HIGHSCORE!!!!!", 32, new Vector3d(400 - (32 * 9), 300 + 48, 0));
        Level level = new Level(uuidFactory.createUUID(), getGame());
        level.init(ticker, brickFactory, score);


        addObject(brickFactory);
        addObject(gameField);
        addObject(cursor);
        addObject(ticker);
        addObject(score);
        addObject(colorChanger);
        addObject(level);

        getGame().getMessageBus().subscribe(GameStateChanged.class, (msg) -> {
            if (msg.getNewGameState() == GameState.GAME_OVER) {
                addObject(gameOverText);
                boolean newHighScore = highScore.addScore(score.getScoreValue());

                if (newHighScore) {
                    addObject(newHighscore);
                }

                HighScoreText highScoreText = new HighScoreText(uuidFactory.createUUID());
                highScoreText.init(uuidFactory, highScore.getHighscore());
                addObject(highScoreText);

                try {
                    File highScoreFile = new File(GameScene.class.getResource(HIGH_SCORE_FILE).toURI());
                    OutputStream outputStream = new FileOutputStream(highScoreFile);
                    highScore.store(outputStream);
                } catch (URISyntaxException | FileNotFoundException | NullPointerException e) {
                    LOGGER.error("Could not store Highscore", e);
                }
            }
        });

        super.init();
    }

    @Override
    public void update(int delta) {
        if (getGameState() == GameState.GAME_OVER && keyboard.isCommandClicked(Confirm.COMMAND)) {
            getGame().getManager().changeScene(StartScene.NAME);
        } else if (keyboard.isCommandClicked(Reject.COMMAND)) {
            getGame().getManager().changeScene(StartScene.NAME);
        }

        if (getGameState() == GameState.BRICKS_FALLING) {
            boolean animationDone = true;
            for (EngineObject child : gameField.children()) {
                if (child.getName().equals(Brick.NAME)) {
                    BrickMoveAnimation animation = (BrickMoveAnimation) child.find(BrickMoveAnimation.NAME).get();
                    if (!animation.isDone()) {
                        animationDone = false;
                        break;
                    }
                }
            }
            if (animationDone) {
                setGameState(GameState.RUNNING);
            }
        }

        super.update(delta);
    }

    @Override
    public void dispose() {
        super.dispose();

        removeAllObjects();
    }

    public void removeBrick(int currentRow, int currentColumn) {
        if (getGameState() == GameState.RUNNING) {
            boolean falling = gameField.removeBrick(currentRow, currentColumn);

            if (falling) {
                setGameState(GameState.BRICKS_FALLING);
            }
        }
    }

    public boolean changeColor() {
        if (getGameState() == GameState.RUNNING) {
            gameField.changeColor(cursor.getCurrentRow(), cursor.getCurrentColumn());

            return true;
        } else {
            return false;
        }
    }
}
