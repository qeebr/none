package none.engine.component.input;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for Commandable.
 */
public class CommandableTest {
    @Test
    public void testIsCommandDown() throws Exception {
        TestCommandable keyboard = new TestCommandable();
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
        TestCommandable keyboard = new TestCommandable();
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
        TestCommandable keyboard = new TestCommandable();
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

    class TestCommandable extends Commandable<Key> {
        private Map<Key, Boolean> keys = new HashMap<>();

        protected TestCommandable() {
            super("TestCommandable", UUID.randomUUID());
        }

        public void setKey(Key key, boolean state) {
            keys.put(key, state);
        }

        @Override
        protected boolean isIdDown(Key keyId) {
            return keys.getOrDefault(keyId, false);
        }
    }

}