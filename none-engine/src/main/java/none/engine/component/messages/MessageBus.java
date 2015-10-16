package none.engine.component.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A MessageBus. Subscribe to get notified when messages are being send.
 */
public class MessageBus {

    private Map<Class<Message>, List<Method>> subscribed = new HashMap<>();


    public <T extends Message> void subscribe(Class<T> message, Method<T> method) {
        List<Method> list = subscribed.getOrDefault(message, null);

        if (list == null) {
            list = new ArrayList<>();
            subscribed.put((Class<Message>) message, list);
        }

        list.add(method);
    }

    public void sendMessage(Message message) {
        List<Method> list = subscribed.getOrDefault(message.getClass(), null);

        if (list != null) {
            list.stream().forEach((item) -> item.doSomething(message));
        }
    }

    public interface Method<T extends Message> {
        void doSomething(T message);
    }
}
