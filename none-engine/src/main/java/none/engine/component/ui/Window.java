package none.engine.component.ui;

import none.engine.Game;

import java.util.UUID;

/**
 * The Ui-Window.
 */
public class Window extends Uiable<UiTexture> {
    public static final String NAME = Window.class.getSimpleName();

    public Window(UUID id, Game game, UiTexture uiTexture) {
        super(NAME, id, game, uiTexture);
    }
}
