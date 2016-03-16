package none.engine.component.ui;

import none.engine.component.renderer.Texture;

/**
 * Interface for Factories, which should creates new Ui-Elements.
 */
public interface UiFactory {

    DimensionFactory<Button> buildButton(String text, Texture upTexture, Texture downTexture);

    <T extends Uiable> DimensionFactory<T> build(Class<T> clazz, Texture texture);

}
