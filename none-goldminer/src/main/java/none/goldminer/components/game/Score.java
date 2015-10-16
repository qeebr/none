package none.goldminer.components.game;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Text;
import none.goldminer.components.messages.GemsDestroyed;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keeping track of user points.
 */
public class Score extends AbsStructObject<EngineObject> {
    public static final Logger LOGGER = LoggerFactory.getLogger(Score.class);
    private Integer scoreValue;
    private Text textField;

    public Score(Game game, EngineObject parent) {
        super(Score.class.getSimpleName(), game.getInjector().getInstance(UUIDFactory.class).createUUID(), game, parent);
        scoreValue = 0;
    }

    public Integer getScoreValue() {
        return scoreValue;
    }

    @Override
    public void init() {
        UUIDFactory uuidFactory = getGame().getInjector().getInstance(UUIDFactory.class);
        getGame().getMessageBus().subscribe(GemsDestroyed.class, (msg) -> {
            scoreValue += msg.getGemCount();
            textField.setText(scoreValue.toString());
        });

        textField = new Text(uuidFactory.createUUID(), scoreValue.toString(), 32, new Vector3d(800 - (32 * 4), 600 - (32 * 2), 0));
        addObject(textField);

        super.init();
    }
}
