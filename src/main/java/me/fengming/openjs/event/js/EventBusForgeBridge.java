package me.fengming.openjs.event.js;

import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public class EventBusForgeBridge {
    public static EventBusForgeBridge create(IEventBus forgeBus) {
        return new EventBusForgeBridge(forgeBus);
    }

    private final IEventBus forgeBus;
    private final List<RegisteredBus<?>> registered = new ArrayList<>();

    protected EventBusForgeBridge(IEventBus forgeBus) {
        this.forgeBus = Objects.requireNonNull(forgeBus);
    }

    public <E extends Event> EventBusForgeBridge bind(EventBus<E> bus) {
        return bind(bus, EventPriority.NORMAL, false);
    }

    public <E extends Event> EventBusForgeBridge bind(EventBusJS<E, ?> bus) {
        return bind(bus.bus(), EventPriority.NORMAL, false);
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
        forgeBus.addListener(priority, receiveCancelled, bus.eventType(), listener);
        registered.add(new RegisteredBus<>(bus, listener));
        return this;
    }

    public boolean unBind(RegisteredBus<?> registered) {
        if (this.registered.remove(registered)) {
            forgeBus.unregister(registered.listener);
            return true;
        }
        return false;
    }

    public List<RegisteredBus<?>> viewRegistered() {
        return Collections.unmodifiableList(this.registered);
    }

    public record RegisteredBus<T>(
        EventBus<T> bus,
        Consumer<T> listener
    ) {
    }
}
