package none.engine.component.ui;

import none.engine.Game;
import none.engine.component.Transform;
import none.engine.component.renderer.primitives.Text;

import java.util.UUID;

/**
 * A Label. Displays an Text.
 */
public class Label extends Uiable {

    private Text textContent;
    private Transform textTransform;

    public Label(String name, String text, UUID id, Game game) {
        super(name, id, game, null);

        this.textContent = new Text(UUID.randomUUID(), text, 40);
        this.textTransform = new Transform(UUID.randomUUID());
    }

    public Text getTextContent() {
        return textContent;
    }

    public Transform getTextTransform() {
        textTransform.getPosition().x = getX();
        textTransform.getPosition().y = getY() - (textContent.getTextSize());

        return textTransform;
    }
}
