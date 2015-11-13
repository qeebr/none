package none.goldminer.components.game;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.primitives.Text;
import none.goldminer.components.messages.GemsDestroyed;
import org.joml.Vector3d;

/**
 * Keeping track of user points.
 */
public class Score extends AbsStructObject<EngineObject> {
    private Integer scoreValue;
    private Renderable textField;

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
            textField.getText().setText(scoreValue.toString());
        });

        Text text = new Text(uuidFactory.createUUID(), scoreValue.toString(), 32);
        TransformComponent position = new TransformComponent(uuidFactory.createUUID(), new Vector3d(800 - (32 * 4), 600 - (32 * 2), 0));

        textField = new Renderable("Score-Textfield", uuidFactory.createUUID(), text, position);
        addObject(textField);

        super.init();
    }
}
