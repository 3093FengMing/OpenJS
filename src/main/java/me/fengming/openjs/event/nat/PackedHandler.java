package me.fengming.openjs.event.nat;

import me.fengming.openjs.script.ScriptType;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.function.Consumer;

/**
 * @author ZZZank
 */
final class PackedHandler<T> implements Consumer<T> {
    final ScriptType type;
    final IEventBus bus;
    final Consumer<T> inner;

    PackedHandler(SidedNativeEvents events, IEventBus bus, Consumer<T> inner) {
        this.type = events.type;
        this.bus = bus;
        this.inner = inner;
    }

    @Override
    public void accept(T event) {
        try {
            inner.accept(event);
        } catch (Exception e) {
            type.logger.error("Error when handling native event", e);
        }
    }
}
