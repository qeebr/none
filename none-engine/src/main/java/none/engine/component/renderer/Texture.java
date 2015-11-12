package none.engine.component.renderer;

import com.google.common.base.Preconditions;
import none.engine.component.AbsObject;
import none.engine.component.assets.Loadable;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Representation from a Texture.
 */
public abstract class Texture extends AbsObject implements Loadable {

    private final String texturePath;
    private final int width;
    private final int height;

    public Texture(String name, UUID id, String texturePath,
                   int width, int height) {
        super(name, id);
        Preconditions.checkArgument(width > 0 && height > 0);
        Preconditions.checkArgument(StringUtils.isNotBlank(texturePath));

        this.texturePath = texturePath;
        this.width = width;
        this.height = height;
    }

    @Override
    public String getSourcePath() {
        return texturePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
