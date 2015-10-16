package none.engine.component.physic;

import none.engine.Game;
import none.engine.scenes.Scene;

/**
 * Disabled Physics.
 */
public class NullMasterPhysics extends MasterPhysic {

    public NullMasterPhysics(Game game) {
        super(game);
    }

    @Override
    public void update(int deltaInMs, Scene currentScene) {
        //Do nothing, you are an Null-PhysicsMaster.
    }
}
