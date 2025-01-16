package me.fengming.openjs.event;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.fengming.openjs.script.ScriptType;
import me.fengming.openjs.utils.Cast;
import me.fengming.openjs.utils.annotations.HideFromJS;
import org.jetbrains.annotations.Nullable;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.util.*;

/**
 * @author FengMing
 */
public class EventHandler<T extends OpenJSEvent> extends BaseFunction {
    private final String name;
    private ScriptType type;

    private final List<EventListener> listeners = new ArrayList<>();
    private final Object2ObjectOpenHashMap<Object, List<EventListener>> extraListener = new Object2ObjectOpenHashMap<>();

    private boolean sortedAndFrozen;

    public EventHandler(String name, ScriptType type) {
        this.name = name;
        this.type = type;
        this.sortedAndFrozen = false;
    }

    public String getName() {
        return this.name;
    }

    @HideFromJS
    public void post(T event) {
        post(event, null);
    }

    /**
     * Post event parameters to the handler in JS, which will be called in priority order
     */
    @HideFromJS
    public void post(T event, @Nullable Object extras) {
        if (!sortedAndFrozen) {
            listeners.sort(EventListener::compareTo);
            for (var list : extraListener.values()) {
                list.sort(EventListener::compareTo);
            }
            sortedAndFrozen = true;
        }
        List<EventListener> localListener = extras == null ? listeners : extraListener.get(extras);
        for (var listener : localListener) {
            listener.handler().onEvent(event);
        }
    }

    public void addListener(IEventHandler handler, int priority, @Nullable Object extras) {
        EventListener listener = new EventListener(handler, priority);
        if (extras == null) {
            listeners.add(listener);
            return;
        }

        if (extraListener.containsKey(extras)) {
            extraListener.get(extras).add(listener);
        } else {
            List<EventListener> l = new ArrayList<>();
            l.add(listener);
            extraListener.put(extras, l);
        }
    }

    public void addListener(IEventHandler handler, int priority) {
        addListener(handler, priority, null);
    }

    public void addListener(IEventHandler handler, Object extras) {
        addListener(handler, 0, extras);
    }

    public void addListener(IEventHandler handler) {
        addListener(handler, 0, null);
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
