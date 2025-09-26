package me.fengming.openjs.event.js;

import me.fengming.openjs.binding.Binding;
import me.fengming.openjs.registry.api.IRegistration;
import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchCancellableEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchKey;
import org.mozilla.javascript.NativeObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public class EventGroupJS implements IRegistration {
    private final String name;
    private final Map<String, EventBusJS<?, ?>> buses;

    public EventGroupJS(String name) {
        this.name = Objects.requireNonNull(name);
        this.buses = new HashMap<>();
    }

    public String name() {
        return name;
    }

    public Map<String, EventBusJS<?, ?>> viewBuses() {
        return Collections.unmodifiableMap(buses);
    }

    private <BUS extends EventBusJS<?, ?>> BUS addBusImpl(String name, BUS bus) {
        if (name == null) {
            throw new IllegalArgumentException("'name' should not be null");
        } else if (this.buses.containsKey(name)) {
            throw new IllegalArgumentException("A bus with name '%s' has already been registered");
        }
        this.buses.put(name, bus);
        return bus;
    }

    public <E> EventBusJS<E, Void> addBus(String name, EventBus<E> bus) {
        return addBusImpl(name, new EventBusJS<>(bus));
    }

    public <E, K> EventBusJS<E, K> addBus(String name, DispatchEventBus<E, K> bus) {
        return addBusImpl(name, new EventBusJS<>(bus));
    }

    public <E> EventBusJS<E, Void> createBus(String name, Class<E> eventType) {
        return createBus(name, eventType, false);
    }

    public <E> EventBusJS<E, Void> createBus(String name, Class<E> eventType, boolean cancellable) {
        return createBus(name, eventType, cancellable, null);
    }

    public <E, K> EventBusJS<E, K> createBus(
        String name,
        Class<E> eventType,
        boolean cancellable,
        DispatchKey<E, K> dispatchKey
    ) {
        return createBus(name, eventType, cancellable, dispatchKey, null);
    }

    public <E, K> EventBusJS<E, K> createBus(
        String name,
        Class<E> eventType,
        boolean cancellable,
        DispatchKey<E, K> dispatchKey,
        Function<Object, K> inputTransformer
    ) {
        EventBus<E> bus;
        if (cancellable) {
            bus = dispatchKey != null
                ? DispatchEventBus.create(eventType, dispatchKey)
                : EventBus.create(eventType);
        } else {
            bus = dispatchKey != null
                ? DispatchCancellableEventBus.create(eventType, dispatchKey)
                : CancellableEventBus.create(eventType);
        }
        return addBusImpl(name, new EventBusJS<>(bus, inputTransformer));
    }

    public <E, K> EventBusJS<E, K> addBus(
        String name,
        DispatchEventBus<E, K> bus,
        Function<Object, K> inputTransformer
    ) {
        return addBusImpl(name, new EventBusJS<>(bus, inputTransformer));
    }

    public <E, K> EventBusJS<E, K> addBus(
        String name,
        DispatchCancellableEventBus<E, K> bus,
        Function<Object, K> inputTransformer
    ) {
        return addBusImpl(name, new EventBusJS<>(bus, inputTransformer));
    }

    public Binding asBinding() {
        var object = new NativeObject();
        for (var entry : this.buses.entrySet()) {
            object.defineProperty(entry.getKey(), entry.getValue(), 0);
        }
        return Binding.create(
            this.name,
            object,
            null,
            type -> this.buses.values().forEach(EventGroupJS::unregisterBus)
        );
    }

    private static <E> void unregisterBus(EventBusJS<E, ?> busJS) {
        var bus = busJS.bus();
        for (var token : busJS.tokens()) {
            bus.unregister(token);
        }
    }
}
