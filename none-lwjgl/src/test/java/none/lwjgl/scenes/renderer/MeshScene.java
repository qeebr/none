package none.lwjgl.scenes.renderer;

import none.engine.Game;
import none.engine.component.Transform;
import none.engine.component.assets.MeshHandler;
import none.engine.component.assets.ModelHandler;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.model.Model;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.Texture;
import none.engine.component.renderer.camera.Camera;
import none.engine.component.renderer.camera.PerspectiveCamera;
import none.engine.component.renderer.primitives.Mesh;
import none.engine.scenes.Scene;
import org.joml.Vector3d;

import java.util.List;

/**
 * Test-Scene for a Simple Mesh.
 */
public class MeshScene extends BaseScene implements Scene {
    public static final String NAME = MeshScene.class.getSimpleName();
    private final UUIDFactory uuidFactory;

    private Model model;
    private Mesh mesh;
    private Texture texture;

    private Camera camera;
    private Renderable renderable;

    public MeshScene(UUIDFactory uuidFactory, Game game, List<String> availableScenes) {
        super(NAME, uuidFactory.createUUID(), game, availableScenes);
        this.uuidFactory = uuidFactory;
    }

    @Override
    public Camera getActiveCamera() {
        return camera;
    }

    @Override
    public void init() {
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(uuidFactory.createUUID(), getGame());
        perspectiveCamera.init();
        perspectiveCamera.lookAt(new Vector3d(2, 2, 5), new Vector3d(2, 2, 0), new Vector3d(0, 1, 0));
        camera = perspectiveCamera;

        MeshHandler meshHandler = getGame().getInjector().getInstance(MeshHandler.class);
        ModelHandler modelHandler = getGame().getInjector().getInstance(ModelHandler.class);
        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);

        model = modelHandler.loadModel("models/handMade.obj");
        mesh = meshHandler.loadMesh(model);
        texture = textureHandler.loadTexture("textures/texture.png");
        Transform transform = new Transform(uuidFactory.createUUID());

        renderable = new Renderable("cube", uuidFactory.createUUID(), mesh, texture, transform);
        addObject(renderable);

        super.init();
    }

    @Override
    public void dispose() {
        MeshHandler meshHandler = getGame().getInjector().getInstance(MeshHandler.class);
        ModelHandler modelHandler = getGame().getInjector().getInstance(ModelHandler.class);
        TextureHandler textureHandler = getGame().getInjector().getInstance(TextureHandler.class);

        meshHandler.disposeMesh(mesh);
        modelHandler.disposeModel(model);
        textureHandler.disposeTexture(texture);

        removeObject(renderable);

        super.dispose();
    }

    @Override
    protected String getInfoMessage() {
        return "Displays a cube in lower left area in screen";
    }
}
