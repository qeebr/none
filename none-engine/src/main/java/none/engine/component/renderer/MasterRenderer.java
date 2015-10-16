package none.engine.component.renderer;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.scenes.Scene;

import java.util.UUID;

/**
 * Draws a Scene.
 */
public abstract class MasterRenderer extends AbsObject {

    public MasterRenderer(String name, UUID objectId, Game game) {
        super(name, objectId, game);
    }

    public abstract void draw(Scene scene);
}
