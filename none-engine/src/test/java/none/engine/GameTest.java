package none.engine;

import com.google.inject.Injector;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Test for Game.
 */
public class GameTest {

    @Test
    public void TestQuitFlag() {
        Game game = new TestableGame(mock(GameOptions.class));
        assertFalse(game.isQuitGame());

        game.run(mock(SceneManager.class), mock(Injector.class));
        assertFalse(game.isQuitGame());

        game.stop();
        assertTrue(game.isQuitGame());
    }

    class TestableGame extends Game {

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

}