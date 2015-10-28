package none.goldminer.components.game;

import com.google.inject.Injector;
import none.engine.Game;
import none.engine.GameOptions;
import none.engine.SceneManager;
import none.engine.component.EngineObject;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.RandomUUID;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Texture;
import none.goldminer.components.game.bricks.Brick;
import none.goldminer.components.game.bricks.BrickColor;
import none.goldminer.components.game.bricks.BrickFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test GameField.
 */
public class GameFieldTest {
    private TestableTextureHandler textureHandler;

    private Game game;
    private BrickFactory factory;
    private GameField gameField;

    @Before
    public void setup() {
        textureHandler = new TestableTextureHandler();

        Injector injectorMock = mock(Injector.class);
        when(injectorMock.getInstance(UUIDFactory.class)).thenReturn(new RandomUUID());
        when(injectorMock.getInstance(TextureHandler.class)).thenReturn(textureHandler);

        game = new TestableGame(mock(GameOptions.class));
        game.run(mock(SceneManager.class), injectorMock);


        factory = new BrickFactory(UUID.randomUUID(), game, mock(EngineObject.class));
        factory.init();

        gameField = new GameField(UUID.randomUUID(), game, factory);
        gameField.init();
    }

    @Test
    public void testRemoveBrick() throws Exception {
        Brick brick1 = new Brick(UUID.randomUUID(), game, gameField, textureHandler.loadTexture("texture.png"), BrickColor.BLUE);
        Brick brick2 = new Brick(UUID.randomUUID(), game, gameField, textureHandler.loadTexture("texture.png"), BrickColor.BLUE);
        gameField.getField()[0][0] = brick1;
        gameField.addObject(brick1);
        gameField.getField()[0][1] = brick2;
        gameField.addObject(brick2);

        gameField.removeBrick(0, 0);
        assertThat(gameField.objectsCount(), is(0));
    }

    @Test
    public void testTick() throws Exception {
        assertThat(gameField.objectsCount(), is(0));

        gameField.tick();

        assertThat(gameField.objectsCount(), is(GameField.MAX_ROWS));

        gameField.tick();

        assertThat(gameField.objectsCount(), is(2 * GameField.MAX_ROWS));
    }

    @Test
    public void testChangeColor() throws Exception {
        BrickColor initialColor = BrickColor.RED;
        Brick brick1 = new Brick(UUID.randomUUID(), game, gameField, textureHandler.loadTexture("texture.png"), initialColor);
        gameField.getField()[0][0] = brick1;
        gameField.addObject(brick1);

        gameField.changeColor(0, 0);
        assertThat(gameField.getField()[0][0].getBrickColor(), is(not(initialColor)));
    }

    private class TestableGame extends Game {
        public TestableGame(GameOptions options) {
            super(options);
        }

        @Override
        protected void init() {

        }

        @Override
        protected void gameLoop() {

        }

        @Override
        protected void dispose() {

        }
    }

    private class TestableTextureHandler implements TextureHandler<TestableTexture> {
        @Override
        public TestableTexture loadTexture(String texture) {
            return new TestableTexture(UUID.randomUUID(), texture, 1, 1);
        }

        @Override
        public void disposeTexture(Texture texture) {
            //just do nothing.
        }
    }

    private class TestableTexture extends Texture {
        public TestableTexture(UUID id, String texturePath, int width, int height) {
            super(TestableTexture.class.getSimpleName(), id, texturePath, width, height);
        }
    }
}