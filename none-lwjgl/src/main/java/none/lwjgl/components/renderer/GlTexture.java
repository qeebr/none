package none.lwjgl.components.renderer;

import none.engine.component.renderer.Texture;

import java.util.UUID;

/**
 * A Texture for OpenGL.
 */
public class GlTexture extends Texture {
    public static final String NAME = "GlTexture";

    private final int textureId;

    public GlTexture(UUID id, String texturePath, int width, int height, int textureId) {
        super(NAME, id, texturePath, width, height);
        this.textureId = textureId;
    }

    public int getTextureId() {
        return textureId;
    }
}
