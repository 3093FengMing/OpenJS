package me.fengming.openjs.utils.eventbus;

import me.fengming.openjs.utils.eventbus.impl.CancellableEventBusImpl;

import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public interface CancellableEventBus<E> extends EventBus<E> {

    static <E> CancellableEventBus<E> create(Class<E> eventType) {
        return new CancellableEventBusImpl<>(eventType);
    }

    EventListenerToken<E> addListener(Predicate<E> listener);

    EventListenerToken<E> addListener(byte priority, Predicate<E> listener);
}
