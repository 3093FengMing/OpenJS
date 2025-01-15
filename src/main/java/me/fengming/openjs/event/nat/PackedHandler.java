package me.fengming.openjs.event.nat;

import net.minecraftforge.eventbus.api.IEventBus;

import java.util.function.Consumer;

/**
 * @author ZZZank
 */
final class PackedHandler<T> implements Consumer<T> {
    private final SidedNativeEvents events;
    final IEventBus bus;
    final Consumer<T> inner;

    PackedHandler(SidedNativeEvents events, IEventBus bus, Consumer<T> inner) {
        this.events = events;
        this.bus = bus;
        this.inner = inner;
    }

    @Override
    public void accept(T event) {
        try {
            inner.accept(event);
        } catch (Exception e) {
            events.type.logger.error("Error when handling native event", e);
        }
    }
}
