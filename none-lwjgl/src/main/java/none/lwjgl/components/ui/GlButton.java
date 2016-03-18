package none.lwjgl.components.ui;

import none.engine.Game;
import none.engine.component.ui.Button;
import none.engine.component.ui.UiTexture;

import java.util.UUID;

/**
 * Button which contains the Gl related informations.
 */
public class GlButton extends Button {
    private GlComponent glComponent;

    public GlButton(String name, String text, UUID id, Game game, UiTexture up, UiTexture down) {
        super(name, text, id, game, up, down);

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
