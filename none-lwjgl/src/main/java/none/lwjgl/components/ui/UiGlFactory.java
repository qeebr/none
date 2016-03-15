package none.lwjgl.components.ui;

import com.google.inject.Inject;
import none.engine.Game;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.Texture;
import none.engine.component.ui.*;

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
    public <T extends Uiable> DimensionFactory<T> build(Class<T> clazz, Texture texture) {
        T uiElement;

        if (clazz.equals(Window.class)) {
            uiElement = (T) new Window("Window", uuidFactory.createUUID(), game, initUiTexture(texture));

        } else if (clazz.equals(Textbox.class)) {
            uiElement = (T) new GlTextbox("Textbox", uuidFactory.createUUID(), game, initUiTexture(texture));

        } else {
            throw new IllegalStateException("Unknown Ui Element");
        }

        return new DimensionFactory<T>(uiElement);
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
