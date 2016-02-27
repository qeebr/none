package none.engine.scenes;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;

import java.util.UUID;

/**
 * Base class for Scenes.
 */
public abstract class BaseScene extends AbsStructObject<EngineObject> implements Scene {

    protected BaseScene(String name, UUID id, Game game) {
        super(name, id, game);
    }
}
