package me.fengming.openjs.utils.eventbus.impl.dispatch;

import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventListenerToken;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchCancellableEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.EventDispatchKey;
import me.fengming.openjs.utils.eventbus.impl.CancellableEventBusImpl;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public class DispatchCancellableEventBusImpl<E, K> extends CancellableEventBusImpl<E>
    implements DispatchCancellableEventBus<E, K> {
    private final EventDispatchKey<E, K> dispatchKey;
    private final Map<K, CancellableEventBus<E>> dispatched;

    public DispatchCancellableEventBusImpl(
        Class<E> eventType,
        EventDispatchKey<E, K> dispatchKey,
        Map<K, CancellableEventBus<E>> dispatched
    ) {
        super(eventType);
        this.dispatchKey = Objects.requireNonNull(dispatchKey);
        this.dispatched = Objects.requireNonNull(dispatched);
    }

    @Override
    public EventDispatchKey<E, K> dispatchKey() {
        return dispatchKey;
    }

    @Override
    public EventListenerToken<E> addListener(K key, byte priority, Predicate<E> listener) {
        return this.dispatched
            .computeIfAbsent(key, k -> new CancellableEventBusImpl<>(this.eventType()))
            .addListener(priority, listener);
    }

    @Override
    public boolean post(E event, K key) {
        if (super.post(event)) {
            return true;
        }

        key = this.dispatchKey.transformInput(key);
        if (key != null) {
            var bus = this.dispatched.get(key);
            return bus != null && bus.post(event);
        }
        return false;
    }
}
