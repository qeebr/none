package none.engine.component.assets;

import none.engine.component.renderer.Texture;

/**
 * Handles Loading Textures in Graphic-Framework.
 */
public interface TextureHandler<T extends Texture> {

    /**
     * Loads the given texture.
     *
     * @param texture Path to texture.
     * @return the id.
     */
    T loadTexture(String texture);

    /**
     * Disposes the texture from the gpu.
     *
     * @param texture texture to dispose.
     */
    void disposeTexture(Texture texture);
}
