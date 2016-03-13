package none.engine.component.ui;

import none.engine.component.renderer.Texture;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A Texture for UI's.
 */
public class UiTexture {
    public static final String UPPER_LEFT_CORNER = "UpperLeftCorner";
    public static final String LOWER_LEFT_CORNER = "lowerLeftCorner";
    public static final String UPPER_RIGHT_CORNER = "upperRightCorner";
    public static final String LOWER_RIGHT_CORNER = "lowerRightCorner";

    public static final String UPPER_MIDDLE = "upperMiddle";
    public static final String LOWER_MIDDLE = "lowerMiddle";
    public static final String RIGHT_MIDDLE = "rightMiddle";
    public static final String LEFT_MIDDLE = "leftMiddle";

    public static final String MIDDLE = "middle";

    private final Texture texture;

    private final Map<String, TexturePart> vertices;

    public UiTexture(Texture texture) {
        this.texture = Objects.requireNonNull(texture);
        this.vertices = new HashMap<>();
    }

    public Texture getTexture() {
        return texture;
    }

    public void setVertices(String name, TexturePart vertices) {
        this.vertices.put(name, vertices);
    }

    public TexturePart getVertices(String name) {
        return vertices.getOrDefault(name, null);
    }
}
