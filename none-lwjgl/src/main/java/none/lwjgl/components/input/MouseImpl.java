package none.lwjgl.components.input;

import none.engine.component.input.Mouse;
import none.engine.component.input.MouseKey;
import org.joml.Vector2d;

/**
 * Implementation from the Mouse.
 */
public class MouseImpl extends Mouse {

    @Override
    public boolean isLocked() {
        //Mouse.
        return false;
    }

    @Override
    public void lock() {

    }

    @Override
    public Vector2d getMousePosition() {
        return null;
    }

    @Override
    protected boolean isMouseDown(MouseKey key) {
        return false;
    }
}
