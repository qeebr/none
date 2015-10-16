package none.engine.component.sound;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.scenes.Scene;

import java.util.UUID;

/**
 * Plays sounds from a scene.
 */
public abstract class MasterPlayer extends AbsObject {

    protected MasterPlayer(String name, UUID id, Game game) {
        super(name, id, game);
    }

    public abstract void play(Scene scene);
}
