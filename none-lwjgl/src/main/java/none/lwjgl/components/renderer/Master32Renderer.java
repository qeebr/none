package none.lwjgl.components.renderer;

import com.google.inject.Inject;
import none.engine.Game;
import none.engine.GameOptions;
import none.engine.component.EngineObject;
import none.engine.component.Transform;
import none.engine.component.assets.ShaderHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.MasterRenderer;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.primitives.Sprite;
import none.engine.component.renderer.primitives.Text;
import none.engine.component.ui.Window;
import none.engine.scenes.Scene;
import org.apache.commons.lang3.Validate;
import org.joml.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Master-Renderer for OpenGL 3.2.
 */
public class Master32Renderer extends MasterRenderer {
    public static final String NAME = "Master32Renderer";

    private static final int MATRIX_4_SIZE = 16;

    private static final float CLEAR_COLOR_RED = 0.4f;
    private static final float CLEAR_COLOR_GREEN = 0.6f;
    private static final float CLEAR_COLOR_BLUE = 0.9f;

    private ShaderHandler shaderHandler;
    private int programId;

    private Camera32Renderer cameraRenderer;
    private Mesh32Renderer meshRenderer;
    private Sprite32Renderer spriteRenderer;
    private Text32Renderer textRenderer;
    private UiRenderer uiRenderer;

    private int modelId;
    private int mvpId;

    private Matrix4f modelMatrix;
    private FloatBuffer modelBuffer;

    private Vector3f tmpVector;
    private Vector3f vectorZ;
    private Vector3f vectorY;
    private Vector3f vectorX;

    private List<Renderable> renderables;
    private Window window;

    @Inject
    public Master32Renderer(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);

        this.renderables = new ArrayList<>();
        window = null;

        this.cameraRenderer = new Camera32Renderer(factory, game);
        this.meshRenderer = new Mesh32Renderer(factory, game);
        this.spriteRenderer = new Sprite32Renderer(factory, game);
        this.textRenderer = new Text32Renderer(factory, game);
        this.uiRenderer = new UiRenderer(factory, game);

        IntBuffer normal = BufferUtils.createIntBuffer(1);
        normal.put(1);
        normal.flip();

        IntBuffer text = BufferUtils.createIntBuffer(1);
        text.put(2);
        text.flip();
    }

    @Override
    public void init() {
        initOpenGl();
        createBuffers();

        cameraRenderer.init();
        meshRenderer.init();
        spriteRenderer.init();

        loadShader();
        extractUniforms();

        textRenderer.init();
        uiRenderer.init();
    }

    private void createBuffers() {
        modelMatrix = new Matrix4f();
        modelBuffer = BufferUtils.createFloatBuffer(MATRIX_4_SIZE);

        tmpVector = new Vector3f();
        vectorZ = new Vector3f(0f, 0f, 1f);
        vectorY = new Vector3f(0f, 1f, 0f);
        vectorX = new Vector3f(1f, 0f, 0f);
    }

    private void extractUniforms() {
        int projectionId = GL20.glGetUniformLocation(programId, "projectionMatrix");
        int viewId = GL20.glGetUniformLocation(programId, "viewMatrix");
        modelId = GL20.glGetUniformLocation(programId, "modelMatrix");

        cameraRenderer.setProjectionId(projectionId);
        cameraRenderer.setViewId(viewId);
    }

    private void initOpenGl() {
        GameOptions options = getGame().getOptions();
        GL11.glClearColor(CLEAR_COLOR_RED, CLEAR_COLOR_GREEN, CLEAR_COLOR_BLUE, 0f);
        GL11.glViewport(0, 0, options.getDisplayWidth(), options.getDisplayHeight());
    }

    private void loadShader() {
        shaderHandler = getGame().getInjector().getInstance(ShaderHandler.class);

        shaderHandler.loadVertexShader("shader/vertex.glsl");
        shaderHandler.loadFragmentShader("shader/fragment.glsl");

        programId = shaderHandler.createProgram();
    }

    @Override
    public void dispose() {
        shaderHandler.disposeProgram(programId);

        cameraRenderer.dispose();
        meshRenderer.dispose();
        spriteRenderer.dispose();
        textRenderer.dispose();
        uiRenderer.dispose();
    }

    @Override
    public void draw(Scene scene) {
        Validate.notNull(scene);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        //Culling
        GL11.glFrontFace(GL11.GL_CCW);
        GL11.glDisable(GL11.GL_CULL_FACE);

        //Depth-Test
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LESS);

        //Blend-Function.
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL20.glUseProgram(programId);

        cameraRenderer.draw(scene.getActiveCamera());

        renderables.clear();
        window = null;
        iterateThroughScene(scene.children());

        renderSpriteAndMeshes();

        setIdentityModel();
        renderText();
        renderUi();
    }

    private void renderUi() {
        uiRenderer.drawUi(window);
    }

    private void renderText() {
        for (Renderable renderable : renderables) {
            Renderable.Type type = renderable.getType();
            Transform transform = renderable.getTransform();
            Text text = renderable.getText();

            if (type == Renderable.Type.TEXT_BASED) {
                textRenderer.draw(text, transform);
            }
        }
    }

    private void renderSpriteAndMeshes() {
        for (Renderable renderable : renderables) {
            Renderable.Type type = renderable.getType();
            GlMesh mesh = (GlMesh) renderable.getMesh();
            GlTexture texture = (GlTexture) renderable.getTexture();
            Transform transform = renderable.getTransform();
            Sprite sprite = renderable.getSprite();

            if (type == Renderable.Type.MESH_BASED) {
                updateModelMatrix(transform, true);
                meshRenderer.draw(mesh, texture);
            } else if (type == Renderable.Type.SPRITE_BASED) {
                updateModelMatrix(transform, false);
                spriteRenderer.draw(sprite, texture);
            }
        }
    }

    private void iterateThroughScene(Iterable<EngineObject> children) {
        //Collects all renderables in EngineObject.
        for (EngineObject child : children) {
            if (child instanceof Renderable) {
                //In case this is a visible Renderable, add to the renderables-list.
                Renderable renderable = (Renderable) child;
                if (renderable.isVisible()) {
                    renderables.add((Renderable) child);
                }
            } else if (child instanceof Window) {
                this.window = (Window) child;
            } else {
                //Make sure to find all Renderables in a scene.
                iterateThroughScene(child.children());
            }
        }
    }

    private void updateModelMatrix(Transform transform, boolean withRotation) {
        Vector3d position = transform.getPosition();

        Matrix4f.setIdentity(modelMatrix);
        tmpVector.set((float) position.x, (float) position.y, (float) position.z);
        Matrix4f.translate(tmpVector, modelMatrix, modelMatrix);
        if (withRotation) {
            Matrix4f.rotate(transform.getRotX(), vectorZ, modelMatrix, modelMatrix);
            Matrix4f.rotate(transform.getRotY(), vectorY, modelMatrix, modelMatrix);
            Matrix4f.rotate(transform.getRotZ(), vectorX, modelMatrix, modelMatrix);
        }

        modelMatrix.store(modelBuffer);
        modelBuffer.flip();

        GL20.glUniformMatrix4(modelId, false, modelBuffer);
    }

    private void setIdentityModel() {
        Matrix4f.setIdentity(modelMatrix);
        modelMatrix.store(modelBuffer);
        modelBuffer.flip();

        GL20.glUniformMatrix4(modelId, false, modelBuffer);
    }
}
