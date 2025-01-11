package me.fengming.openjs.event;

import me.fengming.openjs.script.ScriptType;
import me.fengming.openjs.utils.Cast;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author FengMing
 */
public class EventHandler<T extends OpenJSEvent> extends BaseFunction {
    protected String name;
    protected ScriptType type;

    protected Queue<EventContainer> queue = new PriorityQueue<>(Comparator.comparingInt(EventContainer::priority));

    public EventHandler(String name, ScriptType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Post event parameters to the handler in JS, which will be called in priority order
     */
    public void post(T event) {
        Queue<EventContainer> copied = new PriorityQueue<>(this.queue);
        EventContainer e;
        while ((e = copied.poll()) != null) {
            e.handler().onEvent(event);
        }
    }

    /**
     * Will be called when the Event method is called in the script.
     * The parameter types are
     * Callback as {@link BaseFunction},
     * Priority as {@link Integer} which can be ignored,
     * Extras as an array of {@link Object} which can be ignored.
     * <br> Example: <br>
     * <code>
     * YourEvents.eventTest(e => {...}, 123, "sth1", "sth2")
     * </code>
     * @see Function#call
     */
    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("EventHandler requires at least one argument");
        }

        IEventHandler handler = Cast.to(Context.jsToJava(args[0], IEventHandler.class));
        int priority = 0;
        Object[] extras = null;

        if (args.length >= 2 && args[1] instanceof Number n) {
            priority = n.intValue();
        }
        if (args.length >= 3) {
            extras = Arrays.copyOfRange(args, 2, args.length);
        }
        queue.offer(new EventContainer(handler, extras, priority));

        return super.call(cx, scope, thisObj, args);
    }

    public record EventContainer(IEventHandler handler, Object[] extras, Integer priority) {}
}
