package me.fengming.openjs.utils.eventbus.impl.dispatch;

import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventListenerToken;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchCancellableEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchKey;
import me.fengming.openjs.utils.eventbus.impl.CancellableEventBusImpl;
import me.fengming.openjs.utils.eventbus.impl.NeverCancelListener;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public class DispatchCancellableEventBusImpl<E, K> extends CancellableEventBusImpl<E>
    implements DispatchCancellableEventBus<E, K> {

    private final DispatchKey<E, K> dispatchKey;
    private final Map<K, CancellableEventBus<E>> dispatched;

    public DispatchCancellableEventBusImpl(
        Class<E> eventType,
        DispatchKey<E, K> dispatchKey,
        Map<K, CancellableEventBus<E>> dispatched
    ) {
        super(eventType);
        this.dispatchKey = Objects.requireNonNull(dispatchKey);
        this.dispatched = Objects.requireNonNull(dispatched);
    }

    @Override
    public final DispatchKey<E, K> dispatchKey() {
        return dispatchKey;
    }

    @Override
    public final EventListenerToken<E> addListener(K key, byte priority, Consumer<E> listener) {
        return addListener(key, priority, new NeverCancelListener<>(listener));
    }

    @Override
    public final EventListenerToken<E> addListener(K key, Consumer<E> listener) {
        return addListener(key, (byte) 0, listener);
    }

    @Override
    public final EventListenerToken<E> addListener(K key, byte priority, Predicate<E> listener) {
        return this.dispatched
            .computeIfAbsent(key, k -> new CancellableEventBusImpl<>(this.eventType()))
            .addListener(priority, listener);
    }

    @Override
    public final boolean post(E event, K key) {
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
