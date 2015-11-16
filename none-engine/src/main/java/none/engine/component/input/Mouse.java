package none.engine.component.input;

import org.joml.Vector2d;

import java.util.UUID;

/**
 * The abstraction for a Mouse.
 */
public abstract class Mouse extends Commandable<MouseKey> {

    protected Mouse() {
        super(Mouse.class.getSimpleName(), UUID.randomUUID());
    }

    public abstract boolean isInWindow();

    /**
     * Indicates if the Position from the Mouse is locked inside the Window.
     *
     * @return true when locked on screen.
     */
    public abstract boolean isLocked();

    /**
     * Locks the position from the mouse to stick inside the window.
     */
    public abstract void lock();

    /**
     * Returns the current position from the mouse.
     *
     * @return MousePosition.
     */
    public abstract Vector2d getMousePosition();

    /**
     * Sets the MousePosition.
     *
     * @param x Desired x coord.
     * @param y Desired y coord.
     */
    public abstract void setMousePosition(int x, int y);
}
