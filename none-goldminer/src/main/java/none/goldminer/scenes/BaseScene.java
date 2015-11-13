package none.goldminer.scenes;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.input.Key;
import none.engine.component.input.Keyboard;
import none.engine.component.renderer.camera.Camera;
import none.engine.component.renderer.camera.OrthographicCamera;
import none.engine.scenes.Scene;
import none.goldminer.components.input.Confirm;
import none.goldminer.components.input.Reject;
import org.joml.Vector3d;

/**
 * A base Scene. Keeps track of static camera and also of commands used in every scene.
 */
public class BaseScene extends AbsStructObject<EngineObject> implements Scene {
    private static boolean INIT_ONCE = false;

    protected final UUIDFactory uuidFactory;
    protected Keyboard keyboard;

    private OrthographicCamera camera;

    protected BaseScene(String name, UUIDFactory factory, Game game) {
        super(name, factory.createUUID(), game);

        this.uuidFactory = factory;
    }

    @Override
    public Camera getActiveCamera() {
        return camera;
    }

    @Override
    public void init() {
        int width = getGame().getOptions().getDisplayWidth();
        int height = getGame().getOptions().getDisplayHeight();

        camera = new OrthographicCamera(uuidFactory.createUUID(), getGame());
        camera.setFrustum(-width / 2, width / 2, -height / 2, height / 2, -10, 10);
        camera.lookAt(new Vector3d(width / 2, height / 2, 0), new Vector3d(width / 2, height / 2, -1), new Vector3d(0, 1, 0));

        keyboard = getGame().getInjector().getInstance(Keyboard.class);
        if (!INIT_ONCE) {
            INIT_ONCE = true;
            keyboard.registerCommand(Confirm.COMMAND, Key.ENTER);
            keyboard.registerCommand(Reject.COMMAND, Key.ESCAPE);
        }

        super.init();
    }
}
