package me.fengming.openjs.event;

import me.fengming.openjs.script.ScriptType;
import me.fengming.openjs.utils.Cast;
import me.fengming.openjs.utils.annotations.HideFromJS;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.util.*;

/**
 * @author FengMing
 */
public class EventHandler<T extends OpenJSEvent> extends BaseFunction {
    protected String name;
    protected ScriptType type;
    protected List<EventListener> listeners = new ArrayList<>();

    private boolean sortedAndFrozen;

    public EventHandler(String name, ScriptType type) {
        this.name = name;
        this.type = type;
        this.sortedAndFrozen = false;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Post event parameters to the handler in JS, which will be called in priority order
     */
    @HideFromJS
    public void post(T event) {
        if (!sortedAndFrozen) {
            listeners.sort(EventListener::compareTo);
            sortedAndFrozen = true;
        }
        for (var container : listeners) {
            container.handler().onEvent(event);
        }
    }

    public void addListener(IEventHandler handler) {
        listeners.add(new EventListener(handler, null, 0));
    }

    public void addListener(IEventHandler handler, int priority) {
        listeners.add(new EventListener(handler, null, priority));
    }

    public void addListener(IEventHandler handler, Object extras) {
        listeners.add(new EventListener(handler, extras, 0));
    }

    public void addListener(IEventHandler handler, int priority, Object extras) {
        listeners.add(new EventListener(handler, extras, priority));
    }

    /**
     * Will be called when the Event method is called in the script.
     * The parameter types are
     * Extras as {@link Object} which can be ignored,
     * Callback as {@link BaseFunction}.
     * <br> Example: <br>
     * <code>
     * YourEvents.eventTest("sth1", e => {...})
     * </code>
     * <br>, or
     * <code>
     * YourEvents.eventTest(e => {...})
     * </code>
     * @see Function#call
     */
    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (sortedAndFrozen) {
            throw new IllegalStateException("EventHandler has already been frozen.");
        }
        if (args.length != 1 && args.length != 2) {
            throw new IllegalArgumentException("EventHandler requires 1 or 2 arguments");
        }
        boolean flag = args.length == 2;

        IEventHandler handler = Cast.to(Context.jsToJava(flag ? args[1] : args[0], IEventHandler.class));
        Object extras = flag ? args[0] : null;

        addListener(handler, 0, extras);

        return super.call(cx, scope, thisObj, args);
    }
}
