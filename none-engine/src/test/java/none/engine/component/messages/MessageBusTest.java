package none.engine.component.messages;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test for MessageBus.
 */
public class MessageBusTest {

    private boolean called = false;

    @Test
    public void testMessageBus() throws Exception {
        MessageBus messageBus = new MessageBus();
        called = false;

        messageBus.subscribe(SomeMessage.class, (msg) -> called = true);

        messageBus.sendMessage(new SomeMessage());

        assertTrue(called);
    }

    class SomeMessage implements Message {

    }
}