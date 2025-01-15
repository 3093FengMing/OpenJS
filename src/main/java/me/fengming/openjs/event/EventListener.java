package me.fengming.openjs.event;

import org.jetbrains.annotations.*;

/**
 * @param priority 0 is normal, the smaller the number, the earlier the handler is executed
 * @author FengMing
 */
public record EventListener(@NotNull IEventHandler handler, int priority) implements Comparable<EventListener> {
    @Override
    public int compareTo(@NotNull EventListener o) {
        return Integer.compare(this.priority, o.priority);
    }
}
