package none.engine.component;

import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.KeyboardComponent;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class KeyboardComponentTest {

    @Test
    public void testIsCommandDown() throws Exception {
        TestKeyboardComponent keyboard = new TestKeyboardComponent();
        Command command = new CommandA();

        keyboard.registerCommand(command, Key.I_5);
        keyboard.update(0);
        assertThat(keyboard.isCommandDown(command), is(false));

        keyboard.setKey(Key.I_5, true);
        keyboard.update(0);
        assertThat(keyboard.isCommandDown(command), is(true));

        keyboard.setKey(Key.I_5, true);
        keyboard.update(0);
        assertThat(keyboard.isCommandDown(command), is(true));

        keyboard.setKey(Key.I_5, false);
        keyboard.update(0);
        assertThat(keyboard.isCommandDown(command), is(false));

        keyboard.setKey(Key.I_5, false);
        keyboard.update(0);
        assertThat(keyboard.isCommandDown(command), is(false));
    }

    @Test
    public void testIsCommandClicked() throws Exception {
        TestKeyboardComponent keyboard = new TestKeyboardComponent();
        Command command = new CommandA();
        Key keyId = Key.I;

        keyboard.registerCommand(command, keyId);
        keyboard.update(0);
        assertThat(keyboard.isCommandClicked(command), is(false));

        keyboard.setKey(keyId, false);
        keyboard.update(0);
        assertThat(keyboard.isCommandClicked(command), is(false));

        keyboard.setKey(keyId, true);
        keyboard.update(0);
        assertThat(keyboard.isCommandClicked(command), is(true));

        keyboard.setKey(keyId, true);
        keyboard.update(0);
        assertThat(keyboard.isCommandClicked(command), is(false));
    }

    @Test
    public void testIsCommandReleased() throws Exception {
        TestKeyboardComponent keyboard = new TestKeyboardComponent();
        Command command = new CommandA();
        Key keyId = Key.X;

        keyboard.registerCommand(command, keyId);
        keyboard.update(0);
        assertThat(keyboard.isCommandReleased(command), is(false));

        keyboard.setKey(keyId, true);
        keyboard.update(0);
        assertThat(keyboard.isCommandReleased(command), is(false));

        keyboard.setKey(keyId, false);
        keyboard.update(0);
        assertThat(keyboard.isCommandReleased(command), is(true));

        keyboard.setKey(keyId, false);
        keyboard.update(0);
        assertThat(keyboard.isCommandReleased(command), is(false));
    }

    class CommandA implements Command {

    }

    class TestKeyboardComponent extends KeyboardComponent {
        private Map<Key, Boolean> keys = new HashMap<>();

        public void setKey(Key key, boolean state) {
            keys.put(key, state);
        }

        @Override
        protected boolean isIdDown(Key keyId) {
            return keys.getOrDefault(keyId, false);
        }
    }
}