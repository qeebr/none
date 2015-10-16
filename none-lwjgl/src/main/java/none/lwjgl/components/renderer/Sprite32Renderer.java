package none.lwjgl.components.renderer;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Sprite;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;

/**
 * A Renderer for Sprites.
 */
public class Sprite32Renderer extends AbsObject {
    public static final String NAME = "Sprite32Renderer";

    public static final int VERTEX_BUFFER = 0;
    public static final int TEXTURE_BUFFER = 2;

    private static final int VECTOR_3_SIZE = 3;
    private static final int VECTOR_2_SIZE = 2;

    private static final int LIST_SIZE = 6;

    private FloatBuffer vertexBuffer;
    private FloatBuffer uvBuffer;

    private int vaoId;
    private int verticesId;
    private int uvsId;

    public Sprite32Renderer(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);
    }

    public void init() {
        createBuffers();
        createGlBuffers();
    }

    private void createBuffers() {
        vertexBuffer = BufferUtils.createFloatBuffer(LIST_SIZE * 3);
        for (int i = 0; i < 3 * 6; i++) {
            vertexBuffer.put(0.0f);
        }
        uvBuffer = BufferUtils.createFloatBuffer(LIST_SIZE * 2);
        for (int i = 0; i < 2 * 6; i++) {
            uvBuffer.put(0.0f);
        }
    }

    private void createGlBuffers() {
        vaoId = GL30.glGenVertexArrays();

        GL30.glBindVertexArray(vaoId);

        verticesId = GL15.glGenBuffers();
        GL20.glEnableVertexAttribArray(VERTEX_BUFFER);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticesId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(VERTEX_BUFFER, VECTOR_3_SIZE, GL11.GL_FLOAT, false, 0, 0);

        uvsId = GL15.glGenBuffers();
        GL20.glEnableVertexAttribArray(TEXTURE_BUFFER);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uvsId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, uvBuffer, GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(TEXTURE_BUFFER, VECTOR_2_SIZE, GL11.GL_FLOAT, false, 0, 0);

        GL30.glBindVertexArray(0);
    }

    public void draw(Sprite sprite, GlTexture texture) {
        fillBuffers(sprite);
        drawBuffers(texture);
    }

    private void fillBuffers(Sprite sprite) {
        vertexBuffer.clear();
        uvBuffer.clear();

        float x = -1.0f * (sprite.getWidth() / 2.0f);
        float y = -1.0f * (sprite.getHeight() / 2.0f);
        float z = -sprite.getLayer();
        float width = sprite.getWidth();
        float height = sprite.getHeight();

        vertexBuffer.put(x);
        vertexBuffer.put(y + height);
        vertexBuffer.put(z);

        vertexBuffer.put(x + width);
        vertexBuffer.put(y);
        vertexBuffer.put(z);

        vertexBuffer.put(x + width);
        vertexBuffer.put(y + height);
        vertexBuffer.put(z);

        vertexBuffer.put(x);
        vertexBuffer.put(y + height);
        vertexBuffer.put(z);

        vertexBuffer.put(x);
        vertexBuffer.put(y);
        vertexBuffer.put(z);

        vertexBuffer.put(x + width);
        vertexBuffer.put(y);
        vertexBuffer.put(z);

        height = 1 / (float) sprite.getMaxRows();
        width = 1 / (float) sprite.getMaxColumns();
        float v = (sprite.getRow() / (float) sprite.getMaxRows()) - height;
        float u = (sprite.getColumn() / (float) sprite.getMaxColumns()) - width;

        //First Triangle
        uvBuffer.put(u);
        uvBuffer.put(v);

        uvBuffer.put(u + width);
        uvBuffer.put(v + height);

        uvBuffer.put(u + width);
        uvBuffer.put(v);

        //Second Triangle
        uvBuffer.put(u);
        uvBuffer.put(v);

        uvBuffer.put(u);
        uvBuffer.put(v + height);

        uvBuffer.put(u + width);
        uvBuffer.put(v + height);

        vertexBuffer.flip();
        uvBuffer.flip();
    }

    private void drawBuffers(GlTexture texture) {
        GL30.glBindVertexArray(vaoId);

        //Bind new values to VAO.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticesId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_DYNAMIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uvsId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, uvBuffer, GL15.GL_DYNAMIC_DRAW);

        //Active current Texture.
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, LIST_SIZE);

        GL30.glBindVertexArray(0);
    }

    @Override
    public void dispose() {
        GL30.glBindVertexArray(vaoId);
        GL20.glDisableVertexAttribArray(VERTEX_BUFFER);
        GL20.glDisableVertexAttribArray(TEXTURE_BUFFER);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(verticesId);
        GL15.glDeleteBuffers(uvsId);

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);
    }
}
