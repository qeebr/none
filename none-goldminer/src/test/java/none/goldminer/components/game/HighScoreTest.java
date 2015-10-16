package none.goldminer.components.game;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests the HighScore-class.s
 */
public class HighScoreTest {

    @Test
    public void testSort() {
        HighScore highScore = new HighScore("1,0,2,9,3,8,4,7,5,6");
        int[] scores = highScore.getHighscore();

        //The initial string contains numbers from 0 to 9.
        for (int index = 0; index < scores.length; index++) {
            assertThat(index, is(scores[index]));
        }
    }

    @Test
    public void testAdd() {
        HighScore highScore = new HighScore("0,0,1,2,3,4,6,7,8,9");
        highScore.addScore(5);

        int[] scores = highScore.getHighscore();
        for (int index = 0; index < scores.length; index++) {
            assertThat(index, is(scores[index]));
        }
    }

    @Test
    public void testHighScoreFlag() {
        HighScore highScore = new HighScore("0,0,1,2,3,4,6,7,8,9");

        boolean newHighScore = highScore.addScore(-1);
        assertThat(newHighScore, is(false));

        newHighScore = highScore.addScore(5);
        assertThat(newHighScore, is(false));

        newHighScore = highScore.addScore(10);
        assertThat(newHighScore, is(true));
    }
}