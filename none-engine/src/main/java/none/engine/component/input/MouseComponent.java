package none.engine.component.input;

import none.engine.component.AbsObject;
import org.joml.Vector2d;

import java.util.UUID;

/**
 * The abstraction for a Mouse.
 */
public abstract class MouseComponent extends AbsObject {

    protected MouseComponent() {
        super(MouseComponent.class.getSimpleName(), UUID.randomUUID());
    }

    /**
     * Indicates if the Position from the Mouse is locked at mid screen or not.
     *
     * @return true when locked on screen.
     */
    public abstract boolean isLocked();

    public abstract void lock();

    public abstract Vector2d getMousePosition();

    protected abstract boolean isMouseDown(MouseKey key);
}
