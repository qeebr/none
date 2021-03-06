package none.lwjgl.components.renderer;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.component.Transform;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.TextureMap;
import none.engine.component.renderer.TexturePosition;
import none.engine.component.renderer.primitives.Text;
import org.apache.commons.lang3.Validate;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A Renderer to Render 2D-Text.
 */
public class Text32Renderer extends AbsObject {
    public static final String NAME = "Text32Renderer";

    private GlTexture texture;
    private TextureMap textureMap;

    private int vaoId;
    private int verticesBufferId;
    private int uvBufferId;

    public Text32Renderer(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);
    }

    @Override
    public void init() {
        TextureHandler<GlTexture> textureHandler = this.getGame().getInjector().getInstance(TextureHandler.class);
        texture = textureHandler.loadTexture("fonts/simpleFont.png");
        textureMap = textureHandler.loadTextureMap("fonts/fontMap.json");

        vaoId = GL30.glGenVertexArrays();
        verticesBufferId = GL15.glGenBuffers();
        uvBufferId = GL15.glGenBuffers();
    }

    public void draw(Text text, Transform transform) {
        Validate.notNull(text);
        Validate.notNull(transform);

        draw(text.getText(), text.getTextSize(), transform.getPosition());
    }

    private void draw(String text, int size, Vector3d position) {
        float x = (float) position.x;
        float y = (float) position.y;
        float z = (float) position.z;

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> uvs = new ArrayList<>();

        float width;
        float height;

        for (int i = 0; i < text.length(); i++) {
            TexturePosition part = findPart(text.charAt(i));
            width = size * part.getWidth() / part.getHeight();
            height = size;

            Vector3f vertexUpLeft = new Vector3f(x, y + height, z);
            Vector3f vertexUpRight = new Vector3f(x + width, y + height, z);
            Vector3f vertexDownRight = new Vector3f(x + width, y, z);
            Vector3f vertexDownLeft = new Vector3f(x, y, z);
            x = x + width;


            vertices.add(vertexUpLeft);
            vertices.add(vertexDownLeft);
            vertices.add(vertexUpRight);

            vertices.add(vertexDownRight);
            vertices.add(vertexUpRight);
            vertices.add(vertexDownLeft);

            float uvX = (float) part.getX() / textureMap.getWidth();
            float uvY = (float) part.getY() / textureMap.getHeight();
            float uvWidth = (float) part.getWidth() / textureMap.getWidth();
            float uvHeight = (float) part.getHeight() / textureMap.getHeight();

            Vector2f uvUpLeft = new Vector2f(uvX, uvY);
            Vector2f uvUpRight = new Vector2f(uvX + uvWidth, uvY);
            Vector2f uvDownRight = new Vector2f(uvX + uvWidth, uvY + uvHeight);
            Vector2f uvDownLeft = new Vector2f(uvX, uvY + uvHeight);

            uvs.add(uvUpLeft);
            uvs.add(uvDownLeft);
            uvs.add(uvUpRight);

            uvs.add(uvDownRight);
            uvs.add(uvUpRight);
            uvs.add(uvDownLeft);
        }

        GL30.glBindVertexArray(vaoId);

        FloatBuffer buffer = createBuffer3(vertices);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticesBufferId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);

        buffer = createBuffer2(uvs);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uvBufferId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());

        GL30.glBindVertexArray(vaoId);

        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticesBufferId);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        GL20.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uvBufferId);
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 0, 0);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.size());

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
    }

    private TexturePosition findPart(char c) {
        for (TexturePosition part : textureMap.getPositions()) {
            if (part.getId().equals(String.valueOf(c))) {
                return part;
            }
        }

        return textureMap.getPositions().get(0);
    }

    private FloatBuffer createBuffer2(List<Vector2f> vertices) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.size() * 2);
        for (Vector2f vector : vertices) {
            buffer.put(vector.x);
            buffer.put(vector.y);
        }
        buffer.flip();
        return buffer;
    }

    private FloatBuffer createBuffer3(List<Vector3f> vertices) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.size() * 3);
        for (Vector3f vector : vertices) {
            buffer.put(vector.x);
            buffer.put(vector.y);
            buffer.put(vector.z);
        }
        buffer.flip();
        return buffer;
    }

    @Override
    public void dispose() {
        GL15.glDeleteBuffers(verticesBufferId);
        GL15.glDeleteBuffers(uvBufferId);
        GL30.glDeleteVertexArrays(vaoId);

        TextureHandler<GlTexture> textureHandler = this.getGame().getInjector().getInstance(TextureHandler.class);
        textureHandler.disposeTexture(texture);
    }
}
