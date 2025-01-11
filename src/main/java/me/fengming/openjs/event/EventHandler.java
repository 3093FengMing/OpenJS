package me.fengming.openjs.event;

import me.fengming.openjs.script.ScriptType;
import me.fengming.openjs.utils.Cast;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

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

    public void post(T event) {
        Queue<EventContainer> copied = new PriorityQueue<>(this.queue);
        EventContainer e;
        while ((e = copied.poll()) != null) {
            e.handler().onEvent(event);
        }
    }

    /**
     * Will be called when the Event method is called in the script
     * @see Function#call
     */
    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length == 0 || args.length > 2) {
            throw new IllegalArgumentException("The event handler function can only have one argument.");
        }

        IEventHandler handler = Cast.to(Context.jsToJava(args[0], IEventHandler.class));
        int priority = 0;
        if (args.length != 1 && args[1] instanceof Number n) {
            priority = n.intValue();
        }
        queue.offer(new EventContainer(handler, priority));

        return super.call(cx, scope, thisObj, args);
    }

    public record EventContainer(IEventHandler handler, int priority) {}
}
