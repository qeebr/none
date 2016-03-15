package none.engine.component.ui;

import none.engine.Game;
import none.engine.component.Transform;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.Keyboard;
import none.engine.component.renderer.primitives.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A Textbox. Contains an String.
 */
public class Textbox extends Uiable<UiTexture> {
    public static final int MARGIN = 5;

    private final List<KeyPressed> keys = new ArrayList<>();
    private final ShiftKey shift = new ShiftKey();
    private final BackspaceKey backspace = new BackspaceKey();
    private final String blinky = String.valueOf((char) 1);

    private Text textContent;
    private Transform textPosition;

    private Keyboard keyboard;

    public Textbox(String name, UUID id, Game game, UiTexture uiTexture) {
        super(name, id, game, uiTexture);

        textContent = new Text(UUID.randomUUID(), "", 40);
        textPosition = new Transform(UUID.randomUUID());
    }

    public Text getTextContent() {
        return textContent;
    }

    public Transform getTextPosition() {
        textPosition.getPosition().x = getX();
        textPosition.getPosition().y = getY() - (textContent.getTextSize() + MARGIN);

        return textPosition;
    }

    @Override
    public void init() {
        keyboard = getGame().getInjector().getInstance(Keyboard.class);

        for (int index = 'A'; index <= 'Z'; index++) {
            String character = String.valueOf((char) index);
            KeyPressed pressed = new KeyPressed(character.toLowerCase(), Key.valueOf(character));
            keys.add(pressed);
        }

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        for (KeyPressed key : keys) {
            String character = null;

            if (keyboard.isCommandClicked(key) && keyboard.isCommandDown(shift)) {
                character = key.getKey().toUpperCase();
            } else if (keyboard.isCommandClicked(key)) {
                character = key.getKey().toLowerCase();
            }

            if (character != null) {
                addCharacter(character);
            }
        }

        if (keyboard.isCommandClicked(backspace)) {
            if (textContent.getText().length() != 1) {
                textContent.setText(textContent.getText().substring(0, textContent.getText().length() - 2) + blinky);
            }
        }

        super.update(deltaInMs);
    }

    private void addCharacter(String character) {
        if (textContent.getText().length() == 1) {
            textContent.setText(character + textContent.getText());
        } else {
            textContent.setText(textContent.getText().substring(0, textContent.getText().length() - 1) + character + blinky);
        }
    }

    @Override
    public void dispose() {


        super.dispose();
    }

    @Override
    public void onElementEntered() {
        for (KeyPressed key : keys) {
            keyboard.registerCommand(key, key.getEkey());
        }
        keyboard.registerCommand(shift, Key.LEFT_SHIFT);
        keyboard.registerCommand(backspace, Key.BACKSPACE);

        textContent.setText(textContent.getText() + blinky);
    }

    @Override
    public void onElementLeft() {
        for (KeyPressed key : keys) {
            keyboard.deregisterCommand(key);
        }
        keyboard.deregisterCommand(shift);
        keyboard.deregisterCommand(backspace);

        if (textContent.getText().length() == 1) {
            textContent.setText("");
        } else {
            textContent.setText(textContent.getText().substring(0, textContent.getText().length() - 1));
        }
    }

    class KeyPressed implements Command {
        private final Key ekey;
        private String key;

        public KeyPressed(String key, Key eKey) {
            this.key = key;
            this.ekey = eKey;
        }

        public String getKey() {
            return key;
        }

        public Key getEkey() {
            return ekey;
        }
    }

    class ShiftKey implements Command {

    }

    class BackspaceKey implements Command {

    }
}
