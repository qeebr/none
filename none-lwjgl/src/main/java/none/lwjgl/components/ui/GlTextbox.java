package none.lwjgl.components.ui;

import none.engine.Game;
import none.engine.component.ui.Textbox;
import none.engine.component.ui.UiTexture;

import java.util.UUID;

/**
 * Contains the Vertex Array Object and the Buffer Ids.
 */
public class GlTextbox extends Textbox {
    private GlComponent glComponent;

    public GlTextbox(String name, UUID id, Game game, UiTexture uiTexture) {
        super(name, id, game, uiTexture);

        glComponent = new GlComponent();
    }

    public GlComponent getGlComponent() {
        return glComponent;
    }

    @Override
    public void init() {
        glComponent.init();

        super.init();
    }

    @Override
    public void dispose() {
        glComponent.dispose();

        super.dispose();
    }
}
