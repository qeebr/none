package none.engine.component.ui.factories;

import none.engine.component.renderer.Texture;
import none.engine.component.ui.Button;
import none.engine.component.ui.Label;
import none.engine.component.ui.Textbox;
import none.engine.component.ui.Window;

/**
 * Interface for Factories, which should creates new Ui-Elements.
 */
public interface UiFactory {

    DimensionFactory<Label> builLabel(String name, String text);

    DimensionFactory<Button> buildButton(String name, String buttonText, Texture upTexture, Texture downTexture);

    DimensionFactory<Textbox> buildTextbox(String name, Texture texture);

    DimensionFactory<Window> buildWindow(String name, Texture texture);
}
