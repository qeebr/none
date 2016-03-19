package none.engine.component.ui;

import none.engine.Game;
import none.engine.component.Transform;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.Keyboard;
import none.engine.component.renderer.primitives.Text;

import java.util.UUID;

/**
 * A Textbox. Contains an String.
 */
public class Textbox extends Uiable {
    private final static String BLINKY = String.valueOf((char) 1);
    private static char[] READABLE_CHARACTER = new char[]{'q', 'w', 'e', 'r', 't', 'y', 'u',
            'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z',
            'x', 'c', 'v', 'b', 'n', 'm', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U',
            'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z',
            'X', 'C', 'V', 'B', 'N', 'M', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '0', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*',
            '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[',
            '\\', ']', '^', '_', '`', '{', '|', '}', '~', '€',
    };
    private final BackspaceKey backspace = new BackspaceKey();


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
        textPosition.getPosition().y = getY() - textContent.getTextSize();

        return textPosition;
    }

    @Override
    public void init() {
        keyboard = getGame().getInjector().getInstance(Keyboard.class);

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        for (Character character : keyboard.getCurrentCharacters()) {
            if (isReadable(character)) {
                addCharacter(String.valueOf(character));
            }
        }

        if (keyboard.isCommandClicked(backspace)) {
            if (textContent.getText().length() != 1) {
                textContent.setText(textContent.getText().substring(0, textContent.getText().length() - 2) + BLINKY);
            }
        }

        super.update(deltaInMs);
    }

    private boolean isReadable(Character character) {
        for (Character readable : READABLE_CHARACTER) {
            if (readable.equals(character)) {
                return true;
            }
        }
        return false;
    }

    private void addCharacter(String character) {
        if (textContent.getText().length() == 1) {
            textContent.setText(character + textContent.getText());
        } else {
            textContent.setText(textContent.getText().substring(0, textContent.getText().length() - 1) + character + BLINKY);
        }
    }

    @Override
    public void dispose() {
        keyboard = null;

        super.dispose();
    }

    @Override
    public void onElementEntered() {
        keyboard.registerCommand(backspace, Key.BACKSPACE);

        textContent.setText(textContent.getText() + BLINKY);
    }

    @Override
    public void onElementLeft() {
        keyboard.deregisterCommand(backspace);

        if (textContent.getText().length() == 1) {
            textContent.setText("");
        } else {
            textContent.setText(textContent.getText().substring(0, textContent.getText().length() - 1));
        }
    }

    class BackspaceKey implements Command {

    }
}
