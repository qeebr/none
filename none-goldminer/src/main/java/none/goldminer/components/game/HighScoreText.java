package none.goldminer.components.game;

import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.Transform;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.primitives.Text;
import org.joml.Vector3d;

import java.util.Arrays;
import java.util.UUID;

/**
 * Displayes the HighScore.
 */
public class HighScoreText extends AbsStructObject<EngineObject> {

    public HighScoreText(UUID id) {
        super(HighScoreText.class.getSimpleName(), id);
    }

    public void init(UUIDFactory uuidFactory, int[] highscore) {
        Arrays.sort(highscore);

        for (int index = highscore.length - 1, place = 1; index >= 0; index--, place++) {
            Vector3d position = new Vector3d(0, 600 - 20 * place - 1, 0);

            StringBuilder text = new StringBuilder(String.valueOf(place));
            text.append(')');
            for (int spaceCount = text.length(); spaceCount <= 4; spaceCount++) {
                text.append(' ');
            }
            text.append(String.valueOf(highscore[index]));

            Text renderText = new Text(uuidFactory.createUUID(), text.toString(), 20);
            Transform transform = new Transform(uuidFactory.createUUID(), position);

            addObject(new Renderable("HighScoreTextPlace" + place, uuidFactory.createUUID(), renderText, transform));
        }

        super.init();
    }

    @Override
    public void dispose() {
        super.dispose();

        removeAllObjects();
    }
}
