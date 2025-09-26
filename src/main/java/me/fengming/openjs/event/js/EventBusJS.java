package me.fengming.openjs.event.js;

import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventBus;
import me.fengming.openjs.utils.eventbus.EventListenerToken;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchCancellableEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchEventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
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
public class EventBusJS<EVENT, KEY> implements Callable {
    private final EventBus<EVENT> bus;
    private final List<EventListenerToken<EVENT>> tokens;
    private final Function<Object, KEY> inputTransformer;

    public EventBusJS(EventBus<EVENT> bus) {
        this(bus, null);
    }

    protected EventBusJS(EventBus<EVENT> bus, Function<Object, KEY> inputTransformer) {
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

    public EventBus<EVENT> bus() {
        return bus;
    }

    public List<EventListenerToken<EVENT>> tokens() {
        return tokens;
    }

    public boolean post(EVENT event) {
        return this.bus.post(event);
    }

    public boolean post(EVENT event, KEY key) {
        if (canDispatch()) {
            return ((DispatchEventBus<EVENT, KEY>) bus).post(event, key);
        }
        throw new IllegalStateException("This bus is not dispatchable");
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

    /**
     * Bind this bus with Forge bus. When events are posted on Forge bus, listeners in this bus will also receive the event
     *
     * @param forgeBus Forge bus to bind
     * @return {@code this}
     */
    public EventBusJS<EVENT, KEY> bindTo(IEventBus forgeBus) {
        return bindTo(forgeBus, EventPriority.NORMAL, false);
    }

    /**
     * Bind this bus with Forge bus. When events are posted on Forge bus, listeners in this bus will also receive the event
     *
     * @param forgeBus         Forge bus to bind
     * @param priority         priority of this bus
     * @param receiveCancelled if {@code true}, events previously canceled by other Forge bus listeners can still be received by this bus
     * @return {@code this}
     */
    public <E extends Event> EventBusJS<EVENT, KEY> bindTo(
        IEventBus forgeBus,
        EventPriority priority,
        boolean receiveCancelled
    ) {
        if (!Event.class.isAssignableFrom(this.bus.eventType())) {
            throw new IllegalStateException("This bus is not targeting a forge event");
        }

        var castedBus = (EventBus<E>) bus;

        forgeBus.addListener(
            priority,
            receiveCancelled,
            castedBus.eventType(),
            this.canCancel() ? event -> {
                if (castedBus.post(event)) {
                    event.setCanceled(true);
                }
            } : castedBus::post
        );
        return this;
    }
}
