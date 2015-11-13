package none.engine.component.renderer.primitives;

import com.google.common.base.Preconditions;
import none.engine.component.AbsObject;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * text.
 */
public class Text extends AbsObject {
    public static final String NAME = "Text";
    private String text;
    private int textSize;

    public Text(UUID id, int textSize) {
        this(id, StringUtils.EMPTY, textSize);

    }

    public Text(UUID id, String text, int textSize) {
        super(NAME, id);
        this.text = text;
        this.textSize = textSize;
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
}
