package none.engine.component.ui;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import org.joml.Vector2d;

import java.util.UUID;

/**
 * Base-Class for all UI classes.
 */
public class Uiable extends AbsStructObject<Uiable> {

    private final UiTexture uiTexture;

    private int x;
    private int y;
    private int height;
    private int width;
    private Padding padding;

    public Uiable(String name, UUID id, Game game, UiTexture uiTexture) {
        super(name, id, game);
        this.uiTexture = uiTexture;
        padding = new Padding();
    }

    public UiTexture getUiTexture() {
        return uiTexture;
    }

    public Padding getPadding() {
        return padding;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void onMouseDown() {

    }

    public void onMouseUp() {

    }

    public void onElementEntered() {

    }

    public void onElementLeft() {

    }

    public void onRelease() {

    }

    protected boolean isPositionInside(Vector2d position) {
        return ((getX() <= position.x && getX() + getWidth() > position.x) &&
                (getY() >= position.y && getY() - getHeight() < position.y));
    }
}
