package none.engine.component.renderer;

import com.google.common.base.Preconditions;
import none.engine.component.AbsObject;
import none.engine.component.TransformComponent;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector3d;

import java.util.UUID;

/**
 * text.
 */
public class Text extends AbsObject {
    public static final String NAME = "Text";
    private String text;
    private int textSize;
    private boolean positionBased;
    private TransformComponent transform;
    private Vector3d position;

    public Text(UUID id, String text, int textSize, TransformComponent transform) {
        this(id, text, textSize, false, transform, null);
    }

    public Text(UUID id, String text, int textSize, Vector3d position) {
        this(id, text, textSize, true, null, position);
    }

    private Text(UUID id, String text, int textSize, boolean positionBased,
                 TransformComponent transform, Vector3d position) {
        super(NAME, id);
        this.text = text;
        this.textSize = textSize;
        this.positionBased = positionBased;
        this.transform = transform;
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        Preconditions.checkArgument(StringUtils.isNotBlank(text));
        this.text = text;
    }

    public int getTextSize() {
        return textSize;
    }

    public boolean isPositionBased() {
        return positionBased;
    }

    public TransformComponent getTransform() {
        return transform;
    }

    public Vector3d getPosition() {
        return position;
    }
}
