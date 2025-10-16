package me.fengming.openjs.event.js;

import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public class EventBusForgeBridge {
    public static EventBusForgeBridge create(IEventBus forgeBus) {
        return new EventBusForgeBridge(forgeBus);
    }

    private final IEventBus forgeBus;

    protected EventBusForgeBridge(IEventBus forgeBus) {
        this.forgeBus = Objects.requireNonNull(forgeBus);
    }

    public <E extends Event> EventBusForgeBridge bind(EventBus<E> bus, EventPriority priority, boolean receiveCancelled) {
        Consumer<E> listener;
        if (bus instanceof CancellableEventBus<E>) {
            listener = event -> {
                if (bus.post(event)) {
                    event.setCanceled(true);
                }
            };
        } else {
            listener = bus::post;
        }
        return bindImpl(bus.eventType(), listener, priority, receiveCancelled);
    }

    private <E extends Event> EventBusForgeBridge bindImpl(
        Class<E> eventType,
        Consumer<E> listener,
        EventPriority priority,
        boolean receiveCancelled
    ) {
        forgeBus.addListener(priority, receiveCancelled, eventType, listener);
        return this;
    }

    public <E extends Event> EventBusForgeBridge bind(EventBus<E> bus) {
        return bind(bus, EventPriority.NORMAL, false);
    }

    public <E extends Event> EventBusForgeBridge bind(EventBusJS<E, ?> bus) {
        return bind(bus.bus(), EventPriority.NORMAL, false);
    }

    public <E, E_FORGE extends Event> EventBusForgeBridge bindBridged(
        EventBus<E> bus,
        Function<E_FORGE, E> eventWrapper,
        Class<E_FORGE> eventType,
        EventPriority priority,
        boolean receiveCancelled
    ) {
        Objects.requireNonNull(bus, "EventBus<E> bus == null");
        Objects.requireNonNull(eventWrapper, "Function<E_FORGE, E> eventWrapper == null");
        Consumer<E_FORGE> listener;
        if (bus instanceof CancellableEventBus<E>) {
            listener = e -> {
                if (bus.post(eventWrapper.apply(e))) {
                    e.setCanceled(true);
                }
            };
        } else {
            listener = e -> bus.post(eventWrapper.apply(e));
        }
        return bindImpl(eventType, listener, priority, receiveCancelled);
    }

    public <E, E_FORGE extends Event> EventBusForgeBridge bindBridged(
        EventBus<E> bus,
        Function<E_FORGE, E> eventWrapper,
        Class<E_FORGE> eventType
    ) {
        return bindBridged(bus, eventWrapper, eventType, EventPriority.NORMAL, false);
    }

    public <E, E_FORGE extends Event> EventBusForgeBridge bindBridged(
        EventBusJS<E, ?> bus,
        Function<E_FORGE, E> eventWrapper,
        Class<E_FORGE> eventType
    ) {
        return bindBridged(bus.bus(), eventWrapper, eventType);
    }
}
