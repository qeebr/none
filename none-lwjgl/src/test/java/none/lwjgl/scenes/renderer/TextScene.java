package none.lwjgl.scenes.renderer;

import none.engine.Game;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.CameraComponent;
import none.engine.component.renderer.OrthographicCamera;
import none.engine.component.renderer.Text;
import org.joml.Vector3d;

import java.util.List;

/**
 * Displays a Simple Text.
 */
public class TextScene extends BaseScene {
    public static final String NAME = TextScene.class.getSimpleName();

    private final UUIDFactory uuidFactory;

    private OrthographicCamera camera;

    public TextScene(UUIDFactory factory, Game game, List<String> availableScenes) {
        super(NAME, factory.createUUID(), game, availableScenes);

        this.uuidFactory = factory;
    }

    @Override
    public CameraComponent getActiveCamera() {
        return camera;
    }

    @Override
    public void init() {
        int range = 100;
        camera = new OrthographicCamera(uuidFactory.createUUID(), getGame());
        camera.setFrustum(-range, range, -range, range, -range, range);

        Text text = new Text(uuidFactory.createUUID(), "THIS IS A TEXT, PLEASE SEE ME!", 32, new Vector3d());
        addObject(text);

        super.init();
    }
}
