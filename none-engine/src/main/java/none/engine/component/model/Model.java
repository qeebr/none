package none.engine.component.model;

import none.engine.component.AbsObject;
import none.engine.component.assets.Loadable;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.UUID;

/**
 * A abstract representation from a model.
 */
public class Model extends AbsObject implements Loadable {

    private final String sourcePath;
    private final List<Face> faces;
    private final List<Vertex> vertices;

    public Model(UUID id, String sourcePath, List<Face> faces, List<Vertex> vertices) {
        super(Model.class.getSimpleName(), id);

        this.sourcePath = Validate.notNull(sourcePath);
        this.faces = Validate.notNull(faces);
        this.vertices = Validate.notNull(vertices);
    }

    @Override
    public String getSourcePath() {
        return sourcePath;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}
