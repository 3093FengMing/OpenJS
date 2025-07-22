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
public class EventBusJS<E> extends BaseFunction {
    private final EventBus<E> bus;
    private final List<EventListenerToken<E>> tokens;

    public EventBusJS(EventBus<E> bus) {
        this.bus = Objects.requireNonNull(bus);
        tokens = new ArrayList<>();
    }

    public boolean canCancel() {
        return bus instanceof CancellableEventBus<?>;
    }

    public boolean canDispatch() {
        return bus instanceof DispatchEventBus<?, ?>;
    }

    public EventBus<E> bus() {
        return bus;
    }

    public List<EventListenerToken<E>> tokens() {
        return tokens;
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
                    ? registerDispatchCancellable(args[0], args[1])
                    : registerDispatchCancellable(args[0], null);
            } else {
                token = args.length > 1
                    ? registerDispatch(args[0], args[1])
                    : registerDispatch(args[0], null);
            }
        } else {
            if (canCancel()) {
                token = registerCancellable(args[0]);
            } else {
                token = register(args[0]);
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
}
