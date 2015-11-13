package none.lwjgl.scenes.renderer;

import none.engine.Game;
import none.engine.component.Transform;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.camera.Camera;
import none.engine.component.renderer.camera.OrthographicCamera;
import none.engine.component.renderer.primitives.Text;
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
    public Camera getActiveCamera() {
        return camera;
    }

    @Override
    public void init() {
        int range = 100;
        camera = new OrthographicCamera(uuidFactory.createUUID(), getGame());
        camera.setFrustum(-range, range, -range, range, -range, range);

        String message = "THIS IS A TEXT, PLEASE SEE ME!";
        int textSize = range * 2 / message.length();
        Text text = new Text(uuidFactory.createUUID(), message, textSize);
        Transform transform = new Transform(uuidFactory.createUUID(), new Vector3d(-message.length() * textSize / 2, 0, 0));

        addObject(new Renderable("The Text", uuidFactory.createUUID(), text, transform));

        super.init();
    }

    @Override
    protected String getInfoMessage() {
        return "Displays a Text in the middle of the screen.";
    }
}
