package me.fengming.openjs.utils.eventbus.dispatch;

import me.fengming.openjs.utils.eventbus.EventBus;
import me.fengming.openjs.utils.eventbus.EventListenerToken;
import me.fengming.openjs.utils.eventbus.impl.dispatch.DispatchEventBusImpl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public interface DispatchEventBus<E, K> extends EventBus<E> {

    static <E, K> DispatchEventBus<E, K> create(Class<E> eventType, EventDispatchKey<E, K> dispatchKey) {
        return new DispatchEventBusImpl<>(eventType, dispatchKey, new ConcurrentHashMap<>());
    }

    EventDispatchKey<E, K> dispatchKey();

    EventListenerToken<E> addListener(K key, byte priority, Consumer<E> listener);

    EventListenerToken<E> addListener(K key, Consumer<E> listener);

    boolean post(E event, K key);

    @Override
    default boolean post(E event) {
        return post(event, this.dispatchKey().toKey(event));
    }
}
