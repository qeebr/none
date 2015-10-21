package none.goldminer.components.game;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import none.engine.component.assets.TextureHandler;
import none.engine.component.renderer.Texture;
import none.goldminer.components.game.bricks.Brick;
import none.goldminer.components.game.bricks.BrickColor;
import none.goldminer.components.game.bricks.BrickFactory;
import none.goldminer.components.game.bricks.BrickMoveAnimation;
import none.goldminer.components.messages.GemsDestroyed;
import org.joml.Vector3d;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * The GameField.
 */
public class GameField extends AbsStructObject<EngineObject> {
    public static final String BRICK_SPRITE = "textures/gems_square_merged.png";
    public static final int MINIMUM_BRICK_COUNT = 2;
    public static final String NAME = "GameField";
    public static final int MAX_ROWS = 8;
    public static final int MAX_COLUMNS = 11;

    public static final int FIELD_X = (Brick.BRICK_SIZE / 2) + Brick.BRICK_SIZE;
    public static final int FIELD_Y = (Brick.BRICK_SIZE / 2) + Brick.BRICK_SIZE;

    private static final boolean UNMARKED = false;
    private static final boolean MARKED = true;

    private TextureHandler textureHandler;
    private BrickFactory brickFactory;

    private Brick[][] field;
    private boolean[][] markList;
    private Texture brickTexture;

    public GameField(UUID id, Game game, BrickFactory brickFactory) {
        super(NAME, id, game);

        this.brickFactory = Objects.requireNonNull(brickFactory, "brickFactory");

        this.field = new Brick[MAX_ROWS][MAX_COLUMNS];
        this.markList = new boolean[MAX_ROWS][MAX_COLUMNS];
    }

    @Override
    public void init() {
        textureHandler = getGame().getInjector().getInstance(TextureHandler.class);

        brickTexture = textureHandler.loadTexture(BRICK_SPRITE);

        super.init();
    }

    @Override
    public void dispose() {
        textureHandler.disposeTexture(brickTexture);

        super.dispose();
    }

    public boolean removeBrick(int row, int column) {
        Brick brick = field[row][column];
        if (brick == null) {
            return false;
        }

        //Only remove the bricks when threshold is reached.
        clearMarkField();
        int brickCount = countAllBricks(brick.getBrickColor(), row, column);
        if (brickCount < MINIMUM_BRICK_COUNT) {
            return false;
        }

        //Remove all Bricks with same Color.
        for (int currRow = 0; currRow < MAX_ROWS; currRow++) {
            for (int currCol = 0; currCol < MAX_COLUMNS; currCol++) {
                if (markList[currRow][currCol]) {
                    removeObject(field[currRow][currCol]);
                    field[currRow][currCol].dispose();
                    field[currRow][currCol] = null;
                }
            }
        }

        //Bring Bricks back to ground.
        boolean movingBricks = false;
        for (int currCol = 0; currCol < MAX_COLUMNS; currCol++) {
            int rowCounter = 0;

            for (int currRow = 0; currRow < MAX_ROWS; currRow++) {
                if (field[currRow][currCol] != null) {
                    if (currRow != rowCounter) {
                        moveBrick(currRow, currCol, rowCounter);
                        movingBricks = true;
                    }
                    rowCounter++;
                }
            }
        }

        //Notify everyone, that player scored points :).
        getGame().getMessageBus().sendMessage(new GemsDestroyed(brickCount, 1));

        return movingBricks;
    }

    private void clearMarkField() {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLUMNS; col++) {
                markList[row][col] = UNMARKED;
            }
        }
    }

    public boolean tick() {
        //Game is over, when last row has gems.
        boolean gameOver = false;

        for (int row = 0; row < MAX_ROWS; row++) {
            if (field[row][MAX_COLUMNS - 1] != null) {
                gameOver = true;
                break;
            }
        }

        //And there is no empty column in between.
        if (gameOver) {
            for (int col = 0; col < MAX_COLUMNS; col++) {
                boolean emptyColumn = true;

                for (int row = 0; row < MAX_ROWS; row++) {
                    if (field[row][col] != null) {
                        emptyColumn = false;
                        break;
                    }
                }

                if (emptyColumn) {
                    gameOver = false;
                }
            }
        }

        if (gameOver) {
            return true;
        }

        //Find empty column.
        int emptyColumn = -1;
        for (int column = 0; column < MAX_COLUMNS; column++) {
            boolean empty = true;
            for (int row = 0; row < MAX_ROWS; row++) {
                if (field[row][column] != null) {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                emptyColumn = column;
                break;
            }
        }

        //Move everything for one column.
        for (int column = emptyColumn - 1; column >= 0; column--) {
            for (int row = 0; row < MAX_ROWS; row++) {
                Brick brick = field[row][column];
                if (brick == null) {
                    continue;
                }

                field[row][column + 1] = brick;
                field[row][column] = null;
                updateBrickTransform(brick, row, column + 1);
            }
        }

        //Finally add new row.
        addRow();

        return false;
    }

    private void addRow() {
        //Add new row.
        IntStream.range(0, MAX_ROWS).forEach((row) -> {
            Brick brick = newBrick();
            brick.init();
            updateBrickTransform(brick, row, 0);
            addObject(brick);
            field[row][0] = brick;
        });
    }

    private void moveBrick(int currRow, int currCol, int newRow) {
        Brick brick = field[currRow][currCol];
        field[currRow][currCol] = null;
        field[newRow][currCol] = brick;

        BrickMoveAnimation animation = (BrickMoveAnimation) brick.find(BrickMoveAnimation.NAME).get();
        animation.doAnimation(calcBrickX(currCol), calcBrickY(newRow));
    }

    private int countAllBricks(BrickColor color, int row, int column) {
        Brick brick = field[row][column];
        if (brick == null || brick.getBrickColor() != color || markList[row][column]) {
            return 0;
        }

        int foundBricks = 1;
        markList[row][column] = MARKED;

        if (row - 1 >= 0) {
            foundBricks += countAllBricks(color, row - 1, column);
        }
        if (row + 1 < MAX_ROWS) {
            foundBricks += countAllBricks(color, row + 1, column);
        }

        if (column - 1 >= 0) {
            foundBricks += countAllBricks(color, row, column - 1);
        }
        if (column + 1 < MAX_COLUMNS) {
            foundBricks += countAllBricks(color, row, column + 1);
        }

        return foundBricks;
    }

    private void updateBrickTransform(Brick brick, int row, int column) {
        TransformComponent transform = (TransformComponent) brick.find(TransformComponent.NAME).get();
        transform.getPosition().set(new Vector3d(calcBrickX(column), calcBrickY(row), 0));
    }

    private int calcBrickY(int row) {
        return FIELD_Y + (row * Brick.BRICK_SIZE);
    }

    private int calcBrickX(int column) {
        return FIELD_X + (column * Brick.BRICK_SIZE);
    }

    private Brick newBrick() {
        return brickFactory.generateBrick(brickTexture);
    }

    public void changeColor(int currentRow, int currentColumn) {
        Brick brick = field[currentRow][currentColumn];

        if (brick == null) {
            return;
        }

        removeObject(brick);

        Brick newBrick = newBrick();
        while (newBrick.getBrickColor() == brick.getBrickColor()) {
            newBrick = newBrick();
        }

        newBrick.init();
        field[currentRow][currentColumn] = newBrick;

        updateBrickTransform(newBrick, currentRow, currentColumn);
        addObject(newBrick);
    }
}
