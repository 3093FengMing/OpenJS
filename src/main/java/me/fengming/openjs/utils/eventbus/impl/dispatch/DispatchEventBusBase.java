package me.fengming.openjs.utils.eventbus.impl.dispatch;

import me.fengming.openjs.utils.eventbus.CommonPriority;
import me.fengming.openjs.utils.eventbus.EventBus;
import me.fengming.openjs.utils.eventbus.EventListenerToken;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchKey;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
abstract class DispatchEventBusBase<EVENT, KEY, BUS extends EventBus<EVENT>> {
    private final DispatchKey<EVENT, KEY> dispatchKey;
    protected final BUS mainBus;
    protected final Map<KEY, BUS> dispatched;

    protected DispatchEventBusBase(
        Class<EVENT> eventType,
        DispatchKey<EVENT, KEY> dispatchKey,
        Map<KEY, BUS> dispatched
    ) {
        this.dispatchKey = Objects.requireNonNull(dispatchKey);
        this.mainBus = createBus(eventType);
        this.dispatched = Objects.requireNonNull(dispatched);
    }

    public Class<EVENT> eventType() {
        return mainBus.eventType();
    }

    public DispatchKey<EVENT, KEY> dispatchKey() {
        return dispatchKey;
    }

    protected abstract BUS createBus(Class<EVENT> eventType);

    public EventListenerToken<EVENT> addListener(KEY key, byte priority, Consumer<EVENT> listener) {
        if (key == null) {
            return mainBus.addListener(priority, listener);
        }
        return this.dispatched
            .computeIfAbsent(key, k -> createBus(this.eventType()))
            .addListener(priority, listener);
    }

    public EventListenerToken<EVENT> addListener(KEY key, Consumer<EVENT> listener) {
        return addListener(key, CommonPriority.NORMAL, listener);
    }

    public EventListenerToken<EVENT> addListener(Consumer<EVENT> listener) {
        return addListener(null, CommonPriority.NORMAL, listener);
    }

    public EventListenerToken<EVENT> addListener(byte priority, Consumer<EVENT> listener) {
        return addListener(null, priority, listener);
    }

    public boolean unregister(EventListenerToken<EVENT> token) {
        if (mainBus.unregister(token)) {
            return true;
        }
        return this.dispatched.values()
            .stream()
            .anyMatch(bus -> bus.unregister(token));
    }

    public boolean post(EVENT event, KEY key) {
        if (mainBus.post(event)) {
            return true;
        }

        if (key != null) {
            var dispatchedBus = this.dispatched.get(key);
            return dispatchedBus != null && dispatchedBus.post(event);
        }

        return false;
    }

    public boolean post(EVENT event) {
        return post(event, this.dispatchKey.eventToKey(event));
    }
}
