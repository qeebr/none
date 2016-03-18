package none.engine.component.assets;

import none.engine.component.renderer.Texture;
import none.engine.component.renderer.TextureMap;

/**
 * Handles Loading Textures in Graphic-Framework.
 */
public interface TextureHandler<T extends Texture> {

    /**
     * Loads the given texture.
     *
     * @param texture Path to texture.
     * @return loaded texture.
     */
    T loadTexture(String texture);

    /**
     * Disposes the texture from the gpu.
     *
     * @param texture texture to dispose.
     */
    void disposeTexture(Texture texture);

    /**
     * Loads an Texture-Map.
     *
     * @param textureMap The texture map.
     * @return loaded texture map.
     */
    TextureMap loadTextureMap(String textureMap);
}
