package none.goldminer.components.game.bricks;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.component.EngineObject;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Texture;

import java.util.Random;
import java.util.UUID;

/**
 * Generates Gems, depending on Level.
 */
public class BrickFactory extends AbsObject {
    public static final String NAME = "BrickFactory";

    private final Random random;
    private UUIDFactory factory;
    private int level;

    public BrickFactory(UUID id, Game game, EngineObject parent) {
        super(NAME, id, game, parent);

        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void init() {
        level = 1;

        factory = getGame().getInjector().getInstance(UUIDFactory.class);
    }

    public void increaseLevel() {
        if (level < 5) {
            level++;
        }
    }

    public Brick generateBrick(Texture brickTexture) {
        int brickColor;
        switch (level) {
            case 1:
                brickColor = random.nextInt(4) + 1;
                break;
            case 2:
                brickColor = random.nextInt(5) + 1;
                break;
            case 3:
                brickColor = random.nextInt(6) + 1;
                break;
            case 4:
                brickColor = random.nextInt(7) + 1;
                break;
            case 5:
                brickColor = random.nextInt(8) + 1;
                break;
            default:
                throw new IllegalStateException("Undefined Level");
        }

        return new Brick(factory.createUUID(), getGame(), this, brickTexture, BrickColor.valueOf(brickColor));
    }
}
