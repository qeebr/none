package none.engine.component.ui.factories;

import none.engine.component.ui.Uiable;

import java.util.Objects;

/**
 * Configures the dimension for new Ui-Elements.
 */
public class DimensionFactory<T> {
    private Uiable uiable;

    public DimensionFactory(Uiable uiable) {
        this.uiable = Objects.requireNonNull(uiable);
    }


    public T with(int x, int y, int width, int height) {
        uiable.setX(x);
        uiable.setY(y);
        uiable.setWidth(width);
        uiable.setHeight(height);

        return (T) uiable;
    }
}
