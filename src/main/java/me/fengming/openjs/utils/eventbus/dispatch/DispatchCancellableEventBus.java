package me.fengming.openjs.utils.eventbus.dispatch;

import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventListenerToken;
import me.fengming.openjs.utils.eventbus.impl.dispatch.DispatchCancellableEventBusImpl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public interface DispatchCancellableEventBus<E, K> extends CancellableEventBus<E>, DispatchEventBus<E, K> {

    static <E, K> DispatchCancellableEventBus<E, K> create(Class<E> eventType, DispatchKey<E, K> dispatchKey) {
        return new DispatchCancellableEventBusImpl<>(eventType, dispatchKey, new ConcurrentHashMap<>());
    }

    EventListenerToken<E> addListener(K key, byte priority, Predicate<E> listener);

    EventListenerToken<E> addListener(K key, Predicate<E> listener);

    @Override
    default <E_ extends E, K_ extends K> DispatchCancellableEventBus<E_, K_> castDispatch() {
        return (DispatchCancellableEventBus<E_, K_>) this;
    }
}
