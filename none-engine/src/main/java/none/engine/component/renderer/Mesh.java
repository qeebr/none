package none.engine.component.renderer;

import com.google.common.base.Preconditions;
import none.engine.component.AbsObject;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * A Mesh.
 */
public abstract class Mesh extends AbsObject {
    private final String modelPath;

    protected Mesh(String name, UUID id, String modelPath) {
        super(name, id);
        Preconditions.checkArgument(StringUtils.isNotBlank(modelPath));
        this.modelPath = modelPath;
    }

    public String getModelPath() {
        return modelPath;
    }
}
