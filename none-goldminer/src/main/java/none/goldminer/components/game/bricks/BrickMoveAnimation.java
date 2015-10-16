package none.goldminer.components.game.bricks;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import org.joml.Vector3d;

import java.util.UUID;

/**
 * Moves an TransformComponent from point a to point b in given tim-interval.
 */
public class BrickMoveAnimation extends AbsObject {
    public static final String NAME = "BrickMoveAnimation";
    private static final int ANIMATION_TIME = 500;
    private static final int RUNNING = 0;
    private static final int STOPPED = -1;

    private final TransformComponent transform;
    private Vector3d vector;
    private Vector3d destination;

    private int state = STOPPED;
    private int timer;

    public BrickMoveAnimation(UUID id, Game game, EngineObject parent, TransformComponent transform) {
        super(NAME, id, game, parent);
        this.transform = transform;
    }

    @Override
    public void update(int delta) {
        if (state == RUNNING) {
            timer += delta;
            transform.setPosition(transform.getPosition().add(vector.mul(delta)));
            if (timer >= ANIMATION_TIME) {
                state = STOPPED;
                timer = 0;
                transform.setPosition(destination);
            }
        }
    }

    public void doAnimation(int x, int y) {
        destination = new Vector3d(x, y, 0);
        Vector3d diff = destination.sub(transform.getPosition());
        double velocity = diff.length() / (ANIMATION_TIME);

        vector = diff.normalize().mul(velocity);

        state = RUNNING;
        timer = 0;
    }

    public boolean isDone() {
        return state == STOPPED;
    }
}
