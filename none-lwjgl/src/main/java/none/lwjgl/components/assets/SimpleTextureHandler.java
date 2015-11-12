package none.lwjgl.components.assets;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.matthiasmann.twl.utils.PNGDecoder;
import none.engine.component.assets.Assets;
import none.engine.component.assets.TextureHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Texture;
import none.lwjgl.components.renderer.GlTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Simple PNG TextureHandler.
 */
@Singleton
public class SimpleTextureHandler extends BaseHandler<GlTexture, String> implements TextureHandler<GlTexture> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTextureHandler.class);
    private static final int RGBA_BYTE_COUNT = 4;

    private final UUIDFactory uuidFactory;

    @Inject
    public SimpleTextureHandler(UUIDFactory uuidFactory, Assets assets) {
        super(assets);
        this.uuidFactory = Preconditions.checkNotNull(uuidFactory, "uuidFactory");
    }

    @Override
    public GlTexture loadTexture(String texturePath) {
        GlTexture texture = checkLoaded(texturePath);

        if (texture == null) {
            LOGGER.info("Load Texture: {}", texturePath);

            texture = loadPNGTexture(texturePath);
            addResource(texturePath, texture);
        }

        return texture;
    }

    private GlTexture loadPNGTexture(String texturePath) {
        ByteBuffer textureBuffer;
        int width;
        int height;

        try {
            InputStream stream = getAssets().getClass().getResourceAsStream(texturePath);
            PNGDecoder decoder = new PNGDecoder(stream);

            width = decoder.getWidth();
            height = decoder.getHeight();


            textureBuffer = ByteBuffer.allocateDirect(RGBA_BYTE_COUNT * decoder.getWidth() * decoder.getHeight());
            decoder.decode(textureBuffer, decoder.getWidth() * RGBA_BYTE_COUNT, PNGDecoder.Format.RGBA);
            textureBuffer.flip();

            stream.close();
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected exception during texture-loading", e);
        }

        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureBuffer);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        return new GlTexture(uuidFactory.createUUID(), texturePath, width, height, textureId);
    }

    @Override
    public void disposeTexture(Texture texture) {
        if (!(texture instanceof GlTexture)) {
            throw new IllegalStateException("To disposing texture is not a GlTexture.");
        }
        disposeTexture((GlTexture) texture);
    }

    private void disposeTexture(GlTexture texture) {
        if (dispose(texture, texture.getSourcePath())) {
            LOGGER.info("Dispose Texture: {}", texture.getSourcePath());

            GL11.glDeleteTextures(texture.getTextureId());
        }
    }
}
