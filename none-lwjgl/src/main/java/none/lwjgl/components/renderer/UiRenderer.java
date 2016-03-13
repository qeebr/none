package none.lwjgl.components.renderer;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Texture;
import none.engine.component.ui.*;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Renders the UI.
 */
public class UiRenderer extends AbsObject {
    public static final String NAME = UiRenderer.class.getSimpleName();
    public static final int WINDOWS_SIZE = 54;

    private int windowVaoId;
    private int windowVerticesId;
    private int windowUVId;
    private List<Vector3f> windowVertices;
    private List<Vector2f> windowUVs;
    private FloatBuffer windowVerticesBuffer;
    private FloatBuffer windowUVsBuffer;
    private Vector3f tmpUpLeft = new Vector3f();
    private Vector3f tmpUpRight = new Vector3f();
    private Vector3f tmpDownRight = new Vector3f();
    private Vector3f tmpDownLeft = new Vector3f();
    private Vector2f tmpUvUpLeft = new Vector2f();
    private Vector2f tmpUvUpRight = new Vector2f();
    private Vector2f tmpUvDownRight = new Vector2f();
    private Vector2f tmpUvDownLeft = new Vector2f();

    public UiRenderer(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);
    }

    @Override
    public void init() {
        initWindow();
    }

    private void initWindow() {
        windowVerticesBuffer = BufferUtils.createFloatBuffer(WINDOWS_SIZE * 3);
        windowUVsBuffer = BufferUtils.createFloatBuffer(WINDOWS_SIZE * 2);

        windowVertices = new ArrayList<>(WINDOWS_SIZE);
        windowUVs = new ArrayList<>(WINDOWS_SIZE);

        for (int i = 0; i < WINDOWS_SIZE; i++) {
            windowVertices.add(new Vector3f());
            windowUVs.add(new Vector2f());
        }

        windowVaoId = GL30.glGenVertexArrays();
        windowVerticesId = GL15.glGenBuffers();
        windowUVId = GL15.glGenBuffers();

        GL30.glBindVertexArray(windowVaoId);

        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, windowVerticesId);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        GL20.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, windowUVId);
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 0, 0);

        GL30.glBindVertexArray(0);
    }

    public void drawUi(Window window) {
        drawWindow(window);

        drawChildren(window, 0);
    }

    private void drawChildren(Uiable parent, int layer) {
        Iterable<Uiable> iterator = parent.getObjectIterator();

        for (Uiable uiable : iterator) {
            if (uiable instanceof Button) {
                drawButton((Button) uiable, layer);
            }
        }
    }

    private void drawButton(Button uiable, int layer) {
        //do nothing
    }

    private void drawWindow(Window window) {
        GlTexture texture = (GlTexture) window.getUiTexture().getTexture();
        setupWindowList(window);
        fillWindowBuffers();

        GL30.glBindVertexArray(windowVaoId);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, windowVerticesId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, windowVerticesBuffer, GL15.GL_DYNAMIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, windowUVId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, windowUVsBuffer, GL15.GL_DYNAMIC_DRAW);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, WINDOWS_SIZE);

    }

    private void fillWindowBuffers() {
        windowVerticesBuffer.clear();
        for (Vector3f vector : windowVertices) {
            windowVerticesBuffer.put(vector.x);
            windowVerticesBuffer.put(vector.y);
            windowVerticesBuffer.put(vector.z);
        }
        windowVerticesBuffer.flip();

        windowUVsBuffer.clear();
        for (Vector2f vector : windowUVs) {
            windowUVsBuffer.put(vector.x);
            windowUVsBuffer.put(vector.y);
        }
        windowUVsBuffer.flip();
    }

    private void setupWindowList(Window window) {
        UiTexture uiTexture = window.getUiTexture();
        Texture texture = window.getUiTexture().getTexture();
        float x, y, xWithWidth, yWithHeight;

        int currentIndex = 0;
        TexturePart rectangle = uiTexture.getVertices(UiTexture.UPPER_LEFT_CORNER);

        x = (float) window.getX();
        y = (float) window.getY();
        xWithWidth = (float) (window.getX() + rectangle.getWidth());
        yWithHeight = (float) (window.getY() - rectangle.getHeight());

        addVertex(x, y, xWithWidth, yWithHeight, rectangle, texture, windowVertices, windowUVs, currentIndex);

        //#######################################################################################################################

        currentIndex += 6;
        rectangle = uiTexture.getVertices(UiTexture.UPPER_MIDDLE);

        x = (float) (window.getX() + rectangle.getWidth());
        y = (float) (window.getY());
        xWithWidth = (float) ((window.getX() + window.getWidth()) - rectangle.getWidth());
        yWithHeight = (float) (window.getY() - rectangle.getHeight());

        addVertex(x, y, xWithWidth, yWithHeight, rectangle, texture, windowVertices, windowUVs, currentIndex);

        //#######################################################################################################################

        currentIndex += 6;
        rectangle = uiTexture.getVertices(UiTexture.UPPER_RIGHT_CORNER);

        x = (float) ((window.getX() + window.getWidth()) - rectangle.getWidth());
        y = (float) (window.getY());
        xWithWidth = (float) (window.getX() + window.getWidth());
        yWithHeight = (float) (window.getY() - rectangle.getHeight());

        addVertex(x, y, xWithWidth, yWithHeight, rectangle, texture, windowVertices, windowUVs, currentIndex);

        //#######################################################################################################################

        currentIndex += 6;
        rectangle = uiTexture.getVertices(UiTexture.RIGHT_MIDDLE);

        x = (float) ((window.getX() + window.getWidth()) - rectangle.getWidth());
        y = (float) (window.getY() - rectangle.getHeight());
        xWithWidth = (float) (window.getX() + window.getWidth());
        yWithHeight = (float) ((window.getY() - window.getHeight()) + rectangle.getHeight());

        addVertex(x, y, xWithWidth, yWithHeight, rectangle, texture, windowVertices, windowUVs, currentIndex);

        //#######################################################################################################################

        currentIndex += 6;
        rectangle = uiTexture.getVertices(UiTexture.LOWER_RIGHT_CORNER);

        x = (float) ((window.getX() + window.getWidth()) - rectangle.getWidth());
        y = (float) ((window.getY() - window.getHeight()) + rectangle.getHeight());
        xWithWidth = (float) (window.getX() + window.getWidth());
        yWithHeight = (float) (window.getY() - window.getHeight());

        addVertex(x, y, xWithWidth, yWithHeight, rectangle, texture, windowVertices, windowUVs, currentIndex);

        //#######################################################################################################################

        currentIndex += 6;
        rectangle = uiTexture.getVertices(UiTexture.LOWER_MIDDLE);

        x = (float) (window.getX() + rectangle.getWidth());
        y = (float) ((window.getY() - window.getHeight()) + rectangle.getHeight());
        xWithWidth = (float) ((window.getX() + window.getWidth()) - rectangle.getWidth());
        yWithHeight = (float) (window.getY() - window.getHeight());

        addVertex(x, y, xWithWidth, yWithHeight, rectangle, texture, windowVertices, windowUVs, currentIndex);

        //#######################################################################################################################

        currentIndex += 6;
        rectangle = uiTexture.getVertices(UiTexture.LOWER_LEFT_CORNER);

        x = (float) (window.getX());
        y = (float) ((window.getY() - window.getHeight()) + rectangle.getHeight());
        xWithWidth = (float) (window.getX() + rectangle.getWidth());
        yWithHeight = (float) (window.getY() - window.getHeight());

        addVertex(x, y, xWithWidth, yWithHeight, rectangle, texture, windowVertices, windowUVs, currentIndex);

        //#######################################################################################################################

        currentIndex += 6;
        rectangle = uiTexture.getVertices(UiTexture.LEFT_MIDDLE);

        x = (float) (window.getX());
        y = (float) (window.getY() - rectangle.getHeight());
        xWithWidth = (float) (window.getX() + rectangle.getWidth());
        yWithHeight = (float) ((window.getY() - window.getHeight()) + rectangle.getHeight());

        addVertex(x, y, xWithWidth, yWithHeight, rectangle, texture, windowVertices, windowUVs, currentIndex);

        //#######################################################################################################################

        currentIndex += 6;
        rectangle = uiTexture.getVertices(UiTexture.MIDDLE);

        x = (float) (window.getX() + rectangle.getWidth());
        y = (float) (window.getY() - rectangle.getHeight());
        xWithWidth = (float) ((window.getX() + window.getWidth()) - rectangle.getWidth());
        yWithHeight = (float) ((window.getY() - window.getHeight()) + rectangle.getHeight());

        addVertex(x, y, xWithWidth, yWithHeight, rectangle, texture, windowVertices, windowUVs, currentIndex);
    }

    private void addVertex(float x, float y, float xWithWidth, float yWithHeight,
                           TexturePart rectangle, Texture texture,
                           List<Vector3f> vertices, List<Vector2f> uvs, int currentIndex) {

        float uvX = (float) (rectangle.getX() / texture.getWidth());
        float uvY = (float) (rectangle.getY() / texture.getHeight());
        float uvXWithWidth = (float) ((rectangle.getX() + rectangle.getWidth()) / texture.getWidth());
        float uvYWithHeight = (float) ((rectangle.getY() - rectangle.getHeight()) / texture.getHeight());

        tmpUpLeft.x = x;
        tmpUpLeft.y = y;
        tmpUpLeft.z = 0;

        tmpUpRight.x = xWithWidth;
        tmpUpRight.y = y;
        tmpUpRight.z = 0;

        tmpDownRight.x = xWithWidth;
        tmpDownRight.y = yWithHeight;
        tmpDownRight.z = 0;

        tmpDownLeft.x = x;
        tmpDownLeft.y = yWithHeight;
        tmpDownLeft.z = 0;

        copyValues(tmpUpLeft, vertices.get(currentIndex++));
        copyValues(tmpDownLeft, vertices.get(currentIndex++));
        copyValues(tmpUpRight, vertices.get(currentIndex++));

        copyValues(tmpDownRight, vertices.get(currentIndex++));
        copyValues(tmpUpRight, vertices.get(currentIndex++));
        copyValues(tmpDownLeft, vertices.get(currentIndex));
        currentIndex -= 5;

        tmpUvUpLeft.x = uvX;
        tmpUvUpLeft.y = uvYWithHeight;

        tmpUvUpRight.x = uvXWithWidth;
        tmpUvUpRight.y = uvYWithHeight;

        tmpUvDownRight.x = uvXWithWidth;
        tmpUvDownRight.y = uvY;

        tmpUvDownLeft.x = uvX;
        tmpUvDownLeft.y = uvY;

        copyValues(tmpUvUpLeft, uvs.get(currentIndex++));
        copyValues(tmpUvDownLeft, uvs.get(currentIndex++));
        copyValues(tmpUvUpRight, uvs.get(currentIndex++));

        copyValues(tmpUvDownRight, uvs.get(currentIndex++));
        copyValues(tmpUvUpRight, uvs.get(currentIndex++));
        copyValues(tmpUvDownLeft, uvs.get(currentIndex));
    }

    private void copyValues(Vector3f source, Vector3f destination) {
        destination.x = source.x;
        destination.y = source.y;
        destination.z = source.z;
    }

    private void copyValues(Vector2f source, Vector2f destination) {
        destination.x = source.x;
        destination.y = source.y;
    }

    @Override
    public void dispose() {
        GL15.glDeleteBuffers(windowUVId);
        GL15.glDeleteBuffers(windowVerticesId);
        GL30.glDeleteVertexArrays(windowVaoId);
    }
}
