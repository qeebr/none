package none.engine.component.renderer.primitives;

import none.engine.component.AbsObject;
import none.engine.component.model.Model;
import org.apache.commons.lang3.Validate;

import java.util.UUID;

/**
 * A Mesh.
 */
public abstract class Mesh extends AbsObject {
    private final Model model;

    protected Mesh(String name, UUID id, Model model) {
        super(name, id);
        this.model = Validate.notNull(model);
    }

    public Model getModel() {
        return model;
    }
}
