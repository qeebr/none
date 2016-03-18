package none.lwjgl.components.ui;

import com.google.inject.Inject;
import none.engine.Game;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Texture;
import none.engine.component.ui.*;
import none.engine.component.ui.factories.DimensionFactory;
import none.engine.component.ui.factories.UiFactory;

import java.util.Objects;

/**
 * Creates new OpenGl-Ui-Elements.
 */
public class UiGlFactory implements UiFactory {
    private UUIDFactory uuidFactory;
    private Game game;


    @Inject
    public UiGlFactory(UUIDFactory uuidFactory, Game game) {
        this.uuidFactory = Objects.requireNonNull(uuidFactory);
        this.game = Objects.requireNonNull(game);
    }

    @Override
    public DimensionFactory<Label> builLabel(String name, String text) {
        return new DimensionFactory<>(new Label(name, text, uuidFactory.createUUID(), game));
    }

    @Override
    public DimensionFactory<Button> buildButton(String name, String buttonText, Texture upTexture, Texture downTexture) {
        return new DimensionFactory<>(new GlButton(name, buttonText, uuidFactory.createUUID(), game, initUiTexture(upTexture), initUiTexture(downTexture)));
    }

    @Override
    public DimensionFactory<Textbox> buildTextbox(String name, Texture texture) {
        return new DimensionFactory<>(new GlTextbox(name, uuidFactory.createUUID(), game, initUiTexture(texture)));
    }

    @Override
    public DimensionFactory<Window> buildWindow(String name, Texture texture) {
        return new DimensionFactory<>(new Window(name, uuidFactory.createUUID(), game, initUiTexture(texture)));
    }

    private UiTexture initUiTexture(Texture texture) {
        double third = texture.getWidth() / 3;

        UiTexture uiTexture = new UiTexture(texture);
        uiTexture.setVertices(UiTexture.UPPER_LEFT_CORNER, new TexturePart(0.0, third, third, third));
        uiTexture.setVertices(UiTexture.UPPER_MIDDLE, new TexturePart(third, third, third, third));
        uiTexture.setVertices(UiTexture.UPPER_RIGHT_CORNER, new TexturePart(texture.getWidth() - third, third, third, third));

        uiTexture.setVertices(UiTexture.RIGHT_MIDDLE, new TexturePart(texture.getWidth() - third, texture.getHeight() - third, third, third));

        uiTexture.setVertices(UiTexture.LOWER_RIGHT_CORNER, new TexturePart(texture.getWidth() - third, texture.getHeight(), third, third));
        uiTexture.setVertices(UiTexture.LOWER_MIDDLE, new TexturePart(third, texture.getHeight(), third, third));
        uiTexture.setVertices(UiTexture.LOWER_LEFT_CORNER, new TexturePart(0.0, texture.getHeight(), third, third));

        uiTexture.setVertices(UiTexture.LEFT_MIDDLE, new TexturePart(0.0, texture.getHeight() - third, third, third));

        uiTexture.setVertices(UiTexture.MIDDLE, new TexturePart(third, texture.getHeight() - third, third, third));

        return uiTexture;
    }
}
