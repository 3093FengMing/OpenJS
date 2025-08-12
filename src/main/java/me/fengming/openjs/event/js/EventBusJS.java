package me.fengming.openjs.event.js;

import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventBus;
import me.fengming.openjs.utils.eventbus.EventListenerToken;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchCancellableEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchEventBus;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author ZZZank
 */
public abstract class EventBusJS<EVENT, BUS extends EventBus<EVENT>, KEY> implements Callable {
    private final BUS bus;
    private final List<EventListenerToken<EVENT>> tokens;
    private final Function<Object, KEY> inputTransformer;

    protected EventBusJS(BUS bus) {
        this(bus, null);
    }

    protected EventBusJS(BUS bus, Function<Object, KEY> inputTransformer) {
        this.bus = Objects.requireNonNull(bus);
        this.tokens = new ArrayList<>();
        this.inputTransformer = inputTransformer;
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

    public List<EventListenerToken<EVENT>> tokens() {
        return tokens;
    }

    public boolean post(EVENT event) {
        return this.bus.post(event);
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("EventBus requires at least one arg");
        }
        EventListenerToken<EVENT> token;
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
        return this.tokens.add(token);
    }

    private EventListenerToken<EVENT> register(Object listener) {
        var bus = this.bus;
        return bus.addListener((Consumer<EVENT>) Context.jsToJava(listener, Consumer.class));
    }

    private EventListenerToken<EVENT> registerCancellable(Object listener) {
        var bus = (CancellableEventBus<EVENT>) this.bus;
        return bus.addListener((Predicate<EVENT>) Context.jsToJava(listener, Predicate.class));
    }

    private EventListenerToken<EVENT> registerDispatch(Object listener, Object key) {
        var bus = (DispatchEventBus<EVENT, KEY>) this.bus;
        return bus.addListener(
            this.inputTransformer.apply(key),
            (Consumer<EVENT>) Context.jsToJava(listener, Consumer.class)
        );
    }

    private EventListenerToken<EVENT> registerDispatchCancellable(Object listener, Object key) {
        var bus = (DispatchCancellableEventBus<EVENT, KEY>) this.bus;
        return bus.addListener(
            this.inputTransformer.apply(key),
            (Predicate<EVENT>) Context.jsToJava(listener, Predicate.class)
        );
    }

    public static class Regular<E> extends EventBusJS<E, EventBus<E>, Object> {

        public Regular(EventBus<E> bus) {
            super(bus);
        }
    }

    public static class Cancellable<E> extends EventBusJS<E, CancellableEventBus<E>, Object> {

        public Cancellable(CancellableEventBus<E> bus) {
            super(bus);
        }
    }

    public static class Dispatch<E, K> extends EventBusJS<E, DispatchEventBus<E, K>, K> {

        public Dispatch(DispatchEventBus<E, K> bus) {
            super(bus, bus.dispatchKey().keyType()::cast);
        }

        public Dispatch(DispatchEventBus<E, K> bus, Function<Object, K> inputTransformer) {
            super(bus, inputTransformer);
        }

        public boolean post(E event, K key) {
            return bus().post(event, key);
        }
    }

    public static class DispatchCancellable<E, K> extends EventBusJS<E, DispatchCancellableEventBus<E, K>, K> {

        public DispatchCancellable(DispatchCancellableEventBus<E, K> bus) {
            super(bus, bus.dispatchKey().keyType()::cast);
        }

        public DispatchCancellable(DispatchCancellableEventBus<E, K> bus, Function<Object, K> inputTransformer) {
            super(bus, inputTransformer);
        }

        public boolean post(E event, K key) {
            return bus().post(event, key);
        }
    }

    public static class General<E, K> extends EventBusJS<E, EventBus<E>, K> {

        protected General(EventBus<E> bus, Function<Object, K> inputTransformer) {
            super(bus, inputTransformer);
        }

        public boolean post(E event, K key) {
            if (bus() instanceof DispatchEventBus<E, K> dispatch) {
                return dispatch.post(event, key);
            }
            throw new IllegalArgumentException("This EventBus does not support dispatching event");
        }
    }
}
