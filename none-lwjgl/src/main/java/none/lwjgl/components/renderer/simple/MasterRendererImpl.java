package none.lwjgl.components.renderer.simple;

import com.google.inject.Inject;
import none.engine.Game;
import none.engine.GameOptions;
import none.engine.component.EngineObject;
import none.engine.component.Transform;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.MasterRenderer;
import none.engine.scenes.Scene;
import org.lwjgl.opengl.GL11;

/**
 * Root-Renderer.
 */
public class MasterRendererImpl extends MasterRenderer {
    public static final String NAME = "MasterRendererImpl";

    private PlayerRendererImpl playerRenderer;

    @Inject
    public MasterRendererImpl(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);
        this.playerRenderer = new PlayerRendererImpl(factory, game);
    }

    @Override
    public void init() {
        super.init();
        GameOptions options = getGame().getOptions();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, options.getDisplayHeight(), 0, options.getDisplayWidth(), 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    @Override
    public void draw(Scene scene) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        iterateThroughScene(scene.children());
    }

    private void iterateThroughScene(Iterable<EngineObject> children) {
        Transform transform = null;

        for (EngineObject child : children) {
            if (Transform.NAME.equals(child.getName())) {
                transform = (Transform) child;
            } else {
                iterateThroughScene(child.children());
            }
        }

        if (transform != null) {
            playerRenderer.draw(transform);
        }
    }
}
