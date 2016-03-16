package none.engine.component.ui;

import none.engine.Game;

import java.util.UUID;

/**
 * Ui-Element for Button.
 */
public class Button extends Uiable {
    private UiTexture upTexture;
    private UiTexture downTexture;

    private boolean isDown;

    private Action action;

    public Button(String name, UUID id, Game game, UiTexture up, UiTexture down) {
        super(name, id, game, up);

        isDown = false;
        this.upTexture = up;
        this.downTexture = down;
    }

    @Override
    public UiTexture getUiTexture() {
        if (isDown) {
            return downTexture;
        } else {
            return upTexture;
        }
    }

    public void registerButtonHandler(Action action) {
        this.action = action;
    }


    @Override
    public void onMouseDown() {
        isDown = true;
    }

    @Override
    public void onMouseUp() {
        isDown = false;
    }

    @Override
    public void onElementEntered() {
        isDown = true;
    }

    @Override
    public void onElementLeft() {
        isDown = false;
    }

    @Override
    public void onRelease() {
        isDown = false;

        if (action != null) {
            action.execute();
        }
    }

    public interface Action {
        void execute();
    }
}
