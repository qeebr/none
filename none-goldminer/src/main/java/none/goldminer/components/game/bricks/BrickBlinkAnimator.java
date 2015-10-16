package none.goldminer.components.game.bricks;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.component.renderer.Sprite;

import java.util.Random;
import java.util.UUID;

/**
 * Let the Brick blink at random times.
 */
public class BrickBlinkAnimator extends AbsObject {

    private static final int TIMER_THRESHOLD = 500;

    private final Random random;

    private Sprite sprite;

    private boolean isAnimating;
    private int currentTimer = 0;

    public BrickBlinkAnimator(UUID id, Game game, Brick parent) {
        super(BrickBlinkAnimator.class.getSimpleName(), id, game, parent);
        random = new Random(System.currentTimeMillis());
    }

    public void init(Sprite sprite) {
        super.init();

        this.sprite = sprite;

        isAnimating = false;
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        if (!isAnimating) {
            int rndValue = random.nextInt(10000);

            if (rndValue <= 1) {
                isAnimating = true;
                currentTimer = 0;

                sprite.setRow(2);
            }
        } else {
            currentTimer += delta;
            if (currentTimer >= TIMER_THRESHOLD) {
                isAnimating = false;
                sprite.setRow(1);
            }
        }
    }
}