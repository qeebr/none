package none.goldminer.components.game.bricks;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.TransformComponent;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.Texture;
import none.engine.component.renderer.primitives.Sprite;
import org.joml.Vector3d;

import java.util.UUID;

/**
 * A Single-Brick.
 */
public class Brick extends AbsStructObject<EngineObject> {
    public static final int BRICK_SIZE = 50;
    public static final String NAME = "Brick";
    public static final int SPRITE_LAYER = 2;

    private Texture texture;
    private Sprite sprite;
    private BrickBlinkAnimator blinkAnimator;
    private TransformComponent transform;
    private Renderable renderable;

    private BrickColor brickColor;

    public Brick(UUID id, Game game, EngineObject parent, Texture texture, BrickColor brickColor) {
        super(NAME, id, game, parent);
        this.brickColor = brickColor;
        this.texture = texture;
    }

    @Override
    public void init() {
        UUIDFactory factory = getGame().getInjector().getInstance(UUIDFactory.class);

        sprite = new Sprite(factory.createUUID(), 2, 6, BRICK_SIZE, BRICK_SIZE, SPRITE_LAYER);
        sprite.setColumn(brickColor.getColumn());
        blinkAnimator = new BrickBlinkAnimator(factory.createUUID(), getGame(), this);
        blinkAnimator.init(sprite);
        transform = new TransformComponent(factory.createUUID(), getGame(), this, new Vector3d(), new Vector3d());
        BrickMoveAnimation brickMoveAnimation = new BrickMoveAnimation(factory.createUUID(), getGame(), this, transform);
        renderable = new Renderable("Brick-" + brickColor.toString(), factory.createUUID(), sprite, texture, transform);

        addObject(texture);
        addObject(sprite);
        addObject(blinkAnimator);
        addObject(transform);
        addObject(brickMoveAnimation);
        addObject(renderable);

        super.init();
    }

    public BrickColor getBrickColor() {
        return brickColor;
    }
}
