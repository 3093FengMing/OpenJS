package me.fengming.openjs.event.js;

import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventBus;
import me.fengming.openjs.utils.eventbus.EventListenerToken;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchCancellableEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchEventBus;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public abstract class EventBusJS<E, BUS extends EventBus<E>> extends BaseFunction {
    private final BUS bus;
    private final List<EventListenerToken<E>> tokens;

    protected EventBusJS(BUS bus) {
        this.bus = Objects.requireNonNull(bus);
        tokens = new ArrayList<>();
    }

    public boolean canCancel() {
        return bus instanceof CancellableEventBus<?>;
    }

    public boolean canDispatch() {
        return bus instanceof DispatchEventBus<?, ?>;
    }

    public BUS bus() {
        return bus;
    }

    public List<EventListenerToken<E>> tokens() {
        return tokens;
    }

    public boolean post(E event) {
        return this.bus.post(event);
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("EventBus requires at least one arg");
        }
        EventListenerToken<E> token;
        if (canDispatch()) {
            if (canCancel()) {
                token = args.length > 1
                    ? registerDispatchCancellable(args[1], args[0]) // onEvent("key", (e) => true)
                    : registerCancellable(args[0]); // onEvent((e) => true)
            } else {
                token = args.length > 1
                    ? registerDispatch(args[1], args[0]) // onEvent("key", (e) => {})
                    : register(args[0]); // onEvent((e) => {})
            }
        } else {
            if (canCancel()) {
                token = registerCancellable(args[0]); // onEvent((e) => true)
            } else {
                token = register(args[0]); // onEvent((e) => {})
            }
        }
        this.tokens.add(token);
        return super.call(cx, scope, thisObj, args);
    }

    private EventListenerToken<E> register(Object listener) {
        var bus = this.bus;
        return bus.addListener((Consumer<E>) Context.jsToJava(listener, Consumer.class));
    }

    private EventListenerToken<E> registerCancellable(Object listener) {
        var bus = (CancellableEventBus<E>) this.bus;
        return bus.addListener((Predicate<E>) Context.jsToJava(listener, Predicate.class));
    }

    private <K> EventListenerToken<E> registerDispatch(Object listener, Object key) {
        var bus = (DispatchEventBus<E, K>) this.bus;
        return bus.addListener(
            bus.dispatchKey().transformInput(key),
            (Consumer<E>) Context.jsToJava(listener, Consumer.class)
        );
    }

    private <K> EventListenerToken<E> registerDispatchCancellable(Object listener, Object key) {
        var bus = (DispatchCancellableEventBus<E, K>) this.bus;
        return bus.addListener(
            bus.dispatchKey().transformInput(key),
            (Predicate<E>) Context.jsToJava(listener, Predicate.class)
        );
    }

    public static class Regular<E> extends EventBusJS<E, EventBus<E>> {

        public Regular(EventBus<E> bus) {
            super(bus);
        }
    }

    public static class Cancellable<E> extends EventBusJS<E, CancellableEventBus<E>> {

        public Cancellable(CancellableEventBus<E> bus) {
            super(bus);
        }
    }

    public static class Dispatch<E, K> extends EventBusJS<E, DispatchEventBus<E, K>> {

        public Dispatch(DispatchEventBus<E, K> bus) {
            super(bus);
        }

        public boolean post(E event, K key) {
            return bus().post(event, key);
        }
    }

    public static class DispatchCancellable<E, K> extends EventBusJS<E, DispatchCancellableEventBus<E, K>> {

        public DispatchCancellable(DispatchCancellableEventBus<E, K> bus) {
            super(bus);
        }

        public boolean post(E event, K key) {
            return bus().post(event, key);
        }
    }
}
