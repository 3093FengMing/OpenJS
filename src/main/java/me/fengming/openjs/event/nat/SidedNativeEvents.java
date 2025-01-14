package me.fengming.openjs.event.nat;

import me.fengming.openjs.OpenJSMod;
import me.fengming.openjs.script.ScriptType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

/**
 * @see <a href="https://github.com/ZZZank/EventJS">EventJS (GitHub)</a>
 * @author ZZZank
 */
public final class SidedNativeEvents {
    private static final EnumMap<ScriptType, SidedNativeEvents> BY_TYPE = new EnumMap<>(ScriptType.class);

    public static final SidedNativeEvents STARTUP = new SidedNativeEvents(ScriptType.STARTUP);
    public static final SidedNativeEvents SERVER = new SidedNativeEvents(ScriptType.SERVER);
    public static final SidedNativeEvents CLIENT = new SidedNativeEvents(ScriptType.CLIENT);

    public static SidedNativeEvents byType(@Nonnull ScriptType type) {
        return BY_TYPE.get(type);
    }

    private final List<PackedHandler<?>> packedHandlers = new ArrayList<>();
    public final ScriptType type;

    private SidedNativeEvents(ScriptType type) {
        this.type = type;
        BY_TYPE.put(type, this);
    }

    public void unload() {
        for (var packed : packedHandlers) {
            packed.bus.unregister(packed);
        }
        packedHandlers.clear();
    }

    public <T extends Event> void onEvent(Class<T> eventType, Consumer<T> handler) {
        onEvent(EventPriority.NORMAL, false, eventType, handler);
    }

    public <T extends Event> void onEvent(
        EventPriority priority,
        boolean receiveCancelled,
        Class<T> eventType,
        Consumer<T> handler
    ) {
        var packed = new PackedHandler<>(OpenJSMod.selectBus(eventType), handler);
        packedHandlers.add(packed);
        packed.bus.addListener(priority, receiveCancelled, eventType, packed);
    }

    public <T extends GenericEvent<? extends F>, F> void onGenericEvent(
        Class<F> genericClassFilter,
        Class<T> eventType,
        Consumer<T> handler
    ) {
        onGenericEvent(genericClassFilter, EventPriority.NORMAL, false, eventType, handler);
    }

    public <T extends GenericEvent<? extends F>, F> void onGenericEvent(
        Class<F> genericClassFilter,
        EventPriority priority,
        boolean receiveCancelled,
        Class<T> eventType,
        Consumer<T> handler
    ) {
        var packed = new PackedHandler<>(OpenJSMod.selectBus(eventType), handler);
        packedHandlers.add(packed);
        packed.bus.addGenericListener(genericClassFilter, priority, receiveCancelled, eventType, packed);
    }

    private final class PackedHandler<T> implements Consumer<T> {
        private final IEventBus bus;
        private final Consumer<T> inner;

        private PackedHandler(IEventBus bus, Consumer<T> inner) {
            this.bus = bus;
            this.inner = inner;
        }

        @Override
        public void accept(T event) {
            try {
                inner.accept(event);
            } catch (Exception e) {
                SidedNativeEvents.this.type.logger.error("Error when handling native event", e);
            }
        }
    }

    public int getHandlerCount() {
        return packedHandlers.size();
    }
}