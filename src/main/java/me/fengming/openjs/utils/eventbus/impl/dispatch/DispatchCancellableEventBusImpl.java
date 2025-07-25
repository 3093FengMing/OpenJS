package me.fengming.openjs.utils.eventbus.impl.dispatch;

import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.CommonPriority;
import me.fengming.openjs.utils.eventbus.EventListenerToken;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchCancellableEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchKey;

import java.util.Map;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public final class DispatchCancellableEventBusImpl<E, K> extends DispatchEventBusBase<E, K, CancellableEventBus<E>>
    implements DispatchCancellableEventBus<E, K> {

    public DispatchCancellableEventBusImpl(
        Class<E> eventType,
        DispatchKey<E, K> dispatchKey,
        Map<K, CancellableEventBus<E>> dispatched
    ) {
        super(eventType, dispatchKey, dispatched);
    }

    @Override
    protected CancellableEventBus<E> createBus(Class<E> eventType) {
        return CancellableEventBus.create(eventType);
    }

    @Override
    public EventListenerToken<E> addListener(K key, byte priority, Predicate<E> listener) {
        if (key == null) {
            return mainBus.addListener(priority, listener);
        }
        return this.dispatched
            .computeIfAbsent(key, ignored -> this.createBus(this.eventType()))
            .addListener(priority, listener);
    }

    @Override
    public EventListenerToken<E> addListener(K key, Predicate<E> listener) {
        return addListener(key, CommonPriority.NORMAL, listener);
    }

    @Override
    public EventListenerToken<E> addListener(byte priority, Predicate<E> listener) {
        return addListener(null, priority, listener);
    }

    @Override
    public EventListenerToken<E> addListener(Predicate<E> listener) {
        return addListener(null, CommonPriority.NORMAL, listener);
    }
}
