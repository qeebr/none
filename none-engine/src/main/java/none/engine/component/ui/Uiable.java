package none.engine.component.ui;

import none.engine.Game;
import none.engine.component.AbsStructObject;

import java.util.Objects;
import java.util.UUID;

/**
 * Base-Class for all UI classes.
 */
public class Uiable<T extends UiTexture> extends AbsStructObject<Uiable> {

    private final T uiTexture;

    private int x;
    private int y;
    private int height;
    private int width;
    private int layer;

    public Uiable(String name, UUID id, Game game, T uiTexture) {
        super(name, id, game);
        this.uiTexture = Objects.requireNonNull(uiTexture);
    }

    public T getUiTexture() {
        return uiTexture;
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

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void onMouseDown() {

    }

    public void onElementEntered() {

    }

    public void onElementLeft() {

    }

    public void onRelease() {

    }
}
