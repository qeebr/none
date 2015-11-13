package none.lwjgl.components.renderer.simple;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.component.Transform;
import none.engine.component.common.uuid.UUIDFactory;
import org.joml.Vector3d;

import static org.lwjgl.opengl.GL11.*;

/**
 * Simple PlayerRenderer.
 */
public class PlayerRendererImpl extends AbsObject {
    public static final int PLAYER_WIDTH = 10;
    private static final float PLAYER_COLOR_RED = 0.5f;
    private static final float PLAYER_COLOR_GREEN = 0.5f;
    private static final float PLAYER_COLOR_BLUE = 1.0f;

    public PlayerRendererImpl(UUIDFactory factory, Game game) {
        super("PlayerRendererImpl", factory.createUUID(), game);
    }

    public void draw(Transform transform) {
        Vector3d position = transform.getPosition();

        glColor3f(PLAYER_COLOR_RED, PLAYER_COLOR_GREEN, PLAYER_COLOR_BLUE);
        glBegin(GL_QUADS);
        glVertex2d(position.x - PLAYER_WIDTH, position.y - PLAYER_WIDTH);
        glVertex2d(position.x + PLAYER_WIDTH, position.y - PLAYER_WIDTH);
        glVertex2d(position.x + PLAYER_WIDTH, position.y + PLAYER_WIDTH);
        glVertex2d(position.x - PLAYER_WIDTH, position.y + PLAYER_WIDTH);
        glEnd();
    }
}
