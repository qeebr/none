package none.goldminer.components.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * Keeps track of all HighScores.
 */
public class HighScore {
    public static final String PROPERTY_KEY = "HighScore";
    public static final String PROPERTY_DEFAULT = "0,0,0,0,0,0,0,0,0,0";
    public static final int LAST_SCORE = 0;
    public static final String SPLITTER = ",";

    private static final Logger LOGGER = LoggerFactory.getLogger(HighScore.class);

    private final Properties properties = new Properties();

    private int[] highscore;


    public HighScore(String value) {
        this();

        properties.setProperty(PROPERTY_KEY, value);
        loadHighScore();
    }

    public HighScore(InputStream highScoreStream) {
        this();

        try {
            properties.load(highScoreStream);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Could not load HighScore-File", e);
            properties.setProperty(PROPERTY_KEY, PROPERTY_DEFAULT);
        }
        loadHighScore();
    }

    private HighScore() {
        highscore = new int[10];
    }

    private void loadHighScore() {
        int index = 0;
        for (String score : properties.getProperty(PROPERTY_KEY).split(SPLITTER)) {
            highscore[index++] = Integer.valueOf(score);
        }
        orderHighScore();
    }

    private void orderHighScore() {
        Arrays.sort(highscore);
    }

    public int[] getHighscore() {
        return Arrays.copyOf(highscore, highscore.length);
    }

    public boolean addScore(int score) {
        //In case the score is lower than the last stored quit.
        if (score < highscore[LAST_SCORE]) {
            return false;
        }

        int index;
        int firstReplaced = -1;

        for (index = highscore.length - 1; index >= 0; index--) {
            if (score > highscore[index]) {
                firstReplaced = index;
                break;
            }
        }

        for (; index >= 0; index--) {
            int lowerScore = highscore[index];
            highscore[index] = score;
            score = lowerScore;
        }

        return firstReplaced == highscore.length - 1;
    }

    public void store(OutputStream stream) {
        try {
            StringBuilder builder = new StringBuilder(String.valueOf(highscore[LAST_SCORE]));
            for (int index = LAST_SCORE + 1; index < highscore.length; index++) {
                builder.append(SPLITTER);
                builder.append(String.valueOf(highscore[index]));
            }

            properties.setProperty(PROPERTY_KEY, builder.toString());
            properties.store(stream, "HighScore, do not manipulate. Or ... whatever, its your highscore file");
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Could not save HighScore-File", e);
        }
    }
}
