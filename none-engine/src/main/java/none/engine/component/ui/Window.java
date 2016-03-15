package none.engine.component.ui;

import none.engine.Game;
import none.engine.component.input.Command;
import none.engine.component.input.Mouse;
import none.engine.component.input.MouseKey;

import java.util.UUID;

/**
 * The Ui-Window.
 */
public class Window extends Uiable<UiTexture> {
    private final MouseClick mouseClick = new MouseClick();
    private Mouse mouse;

    private Uiable currentUi;

    public Window(String name, UUID id, Game game, UiTexture uiTexture) {
        super(name, id, game, uiTexture);
    }

    @Override
    public void init() {
        mouse = getGame().getInjector().getInstance(Mouse.class);
        mouse.registerCommand(mouseClick, MouseKey.LEFT);

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        if (mouse.isCommandClicked(mouseClick) && isPositionInside(mouse.getMousePosition())) {
            onMouseDown();
        } else if (mouse.isCommandReleased(mouseClick) && isPositionInside(mouse.getMousePosition())) {
            onMouseUp();
        }

        super.update(deltaInMs);
    }

    @Override
    public void dispose() {
        mouse.deregisterCommand(mouseClick);
        mouse = null;

        super.dispose();
    }

    @Override
    public void onMouseDown() {
        Uiable newCurrent = getClickedChild();

        if (currentUi != null) {
            currentUi.onElementLeft();
        }
        if (newCurrent != null) {
            newCurrent.onElementEntered();
        }

        currentUi = newCurrent;
    }

    @Override
    public void onMouseUp() {
        Uiable newCurrent = getClickedChild();

        if (newCurrent == currentUi && currentUi != null) {
            currentUi.onRelease();
        } else if (currentUi != null) {
            currentUi.onElementLeft();
            currentUi = null;
        }
    }

    private Uiable getClickedChild() {
        Uiable newCurrent = null;

        for (Uiable child : getObjectIterator()) {
            if (child.isPositionInside(mouse.getMousePosition())) {
                newCurrent = child;
                break;
            }
        }
        return newCurrent;
    }

    class MouseClick implements Command {

    }
}
