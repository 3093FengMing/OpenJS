package me.fengming.openjs.event.js;

import me.fengming.openjs.binding.Binding;
import me.fengming.openjs.registry.api.IRegistration;
import org.mozilla.javascript.NativeObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            throw new IllegalArgumentException(String.format("A bus with name '%s' has already been registered", name));
        }
        this.buses.put(name, bus);
        return bus;
    }

    public <E, K> EventBusJS<E, K> addBus(String name, EventBusJS<E, K> bus) {
        return addBusImpl(name, bus);
    }

    public <E> EventBusJS<E, Void> addBus(String name, Class<E> eventType) {
        return addBusImpl(name, EventBusJS.of(eventType));
    }

    public <E> EventBusJS<E, Void> addBus(String name, Class<E> eventType, boolean cancellable) {
        return addBusImpl(name, cancellable ? EventBusJS.ofCancellable(eventType) : EventBusJS.of(eventType));
    }

    public Binding asBinding() {
        var object = new NativeObject();
        for (var entry : this.buses.entrySet()) {
            object.put(entry.getKey(), object, entry.getValue());
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
