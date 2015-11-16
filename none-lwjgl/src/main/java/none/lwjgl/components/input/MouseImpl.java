package none.lwjgl.components.input;

import com.google.inject.Singleton;
import none.engine.component.input.Mouse;
import none.engine.component.input.MouseKey;
import org.joml.Vector2d;

/**
 * Implementation from the Mouse.
 */
@Singleton
public class MouseImpl extends Mouse {
    private Vector2d mousePosition;

    public MouseImpl() {
        this.mousePosition = new Vector2d();
    }

    @Override
    public boolean isLocked() {
        return org.lwjgl.input.Mouse.isGrabbed();
    }

    @Override
    public void lock() {
        //toggle it, it's a test anyway.
        org.lwjgl.input.Mouse.setGrabbed(!org.lwjgl.input.Mouse.isGrabbed());
    }

    @Override
    public boolean isInWindow() {
        return org.lwjgl.input.Mouse.isInsideWindow();
    }

    @Override
    public Vector2d getMousePosition() {
        mousePosition.x = org.lwjgl.input.Mouse.getX();
        mousePosition.y = org.lwjgl.input.Mouse.getY();

        return mousePosition;
    }

    @Override
    public void setMousePosition(int x, int y) {
        org.lwjgl.input.Mouse.setCursorPosition(x, y);
    }

    @Override
    protected boolean isIdDown(MouseKey key) {
        if (key == MouseKey.LEFT) {
            return org.lwjgl.input.Mouse.isButtonDown(0);
        } else if (key == MouseKey.RIGHT) {
            return org.lwjgl.input.Mouse.isButtonDown(1);
        } else {
            return false;
        }
    }
}
