package me.fengming.openjs.event.js;

import me.fengming.openjs.binding.base.Binding;
import me.fengming.openjs.registry.api.IRegistration;
import me.fengming.openjs.script.ScriptType;
import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchCancellableEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchEventBus;
import org.mozilla.javascript.ScriptableObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public class EventGroupJS implements IRegistration {
    private final String name;
    private final Map<String, EventBusJS<?, ?, ?>> buses;

    public EventGroupJS(String name) {
        this.name = Objects.requireNonNull(name);
        this.buses = new HashMap<>();
    }

    public String name() {
        return name;
    }

    public Map<String, EventBusJS<?, ?, ?>> buses() {
        return buses;
    }

    private <BUS extends EventBusJS<?, ?, ?>> BUS addBusImpl(String name, BUS bus) {
        if (name == null) {
            throw new IllegalArgumentException("'name' should not be null");
        } else if (this.buses.containsKey(name)) {
            throw new IllegalArgumentException("A bus with name '%s' has already been registered");
        }
        this.buses.put(name, bus);
        return bus;
    }

    public <E> EventBusJS.Regular<E> addBus(String name, EventBus<E> bus) {
        return addBusImpl(name, new EventBusJS.Regular<>(bus));
    }

    public <E> EventBusJS.Cancellable<E> addBus(String name, CancellableEventBus<E> bus) {
        return addBusImpl(name, new EventBusJS.Cancellable<>(bus));
    }

    public <E, K> EventBusJS.Dispatch<E, K> addBus(String name, DispatchEventBus<E, K> bus) {
        return addBusImpl(name, new EventBusJS.Dispatch<>(bus));
    }

    public <E, K> EventBusJS.DispatchCancellable<E, K> addBus(String name, DispatchCancellableEventBus<E, K> bus) {
        return addBusImpl(name, new EventBusJS.DispatchCancellable<>(bus));
    }

    public <E, K> EventBusJS.Dispatch<E, K> addBus(
        String name,
        DispatchEventBus<E, K> bus,
        Function<Object, K> inputTransformer
    ) {
        return addBusImpl(name, new EventBusJS.Dispatch<>(bus, inputTransformer));
    }

    public <E, K> EventBusJS.DispatchCancellable<E, K> addBus(
        String name,
        DispatchCancellableEventBus<E, K> bus,
        Function<Object, K> inputTransformer
    ) {
        return addBusImpl(name, new EventBusJS.DispatchCancellable<>(bus, inputTransformer));
    }

    public Binding asBinding() {
        var thiz = this;
        return new Binding() {
            @Override
            public String name() {
                return thiz.name;
            }

            @Override
            public Object value() {
                var obj = new EventGroupJSObject();
                for (var entry : thiz.buses.entrySet()) {
                    ScriptableObject.putProperty(obj, entry.getKey(), entry.getValue());
                }
                return obj;
            }

            @Override
            public void onUnload(ScriptType scriptType) {
                thiz.buses.values().forEach(EventGroupJS::unregisterBus);
            }
        };
    }

    private static <E, BUS extends EventBus<E>> void unregisterBus(EventBusJS<E, BUS, ?> busJS) {
        var bus = busJS.bus();
        for (var token : busJS.tokens()) {
            bus.unregister(token);
        }
    }

    private static final class EventGroupJSObject extends ScriptableObject {

        @Override
        public String getClassName() {
            return "EventGroupJS";
        }
    }
}
