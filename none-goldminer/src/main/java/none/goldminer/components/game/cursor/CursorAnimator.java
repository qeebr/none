package none.goldminer.components.game.cursor;

import none.engine.component.AbsObject;
import none.engine.component.renderer.primitives.Sprite;

import java.util.UUID;

/**
 * Animates the Cursor-Sprite.
 */
public class CursorAnimator extends AbsObject {
    public static final String NAME = "CursorAnimator";
    public static final int TIME_THRESHOLD = 100;

    private final Sprite sprite;

    private int currentState;
    private int timeDiff;

    public CursorAnimator(UUID id, Sprite sprite) {
        super(NAME, id);
        this.sprite = sprite;
    }

    @Override
    public void init() {
        currentState = -1;
    }

    @Override
    public void update(int deltaInMs) {
        switch (currentState) {
            //Do nothing.
            case -1:
                break;
            //Set to Middle. Clear all values.
            case 0:
                sprite.setColumn(2);
                currentState = 1;
                timeDiff = 0;
                break;
            //Wait, then set to Down. Clear all values.
            case 1:
                timeDiff += deltaInMs;
                if (timeDiff >= TIME_THRESHOLD) {
                    sprite.setColumn(3);
                    currentState = 2;
                    timeDiff = 0;
                }
                break;
            //Wait, then set to Up. Clear all values.
            case 2:
                timeDiff += deltaInMs;
                if (timeDiff >= TIME_THRESHOLD) {
                    sprite.setColumn(1);
                    currentState = -1;
                    timeDiff = 0;
                }
                break;
        }
    }

    public void doAnimation() {
        currentState = 0;
    }
}
