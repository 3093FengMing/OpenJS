package me.fengming.openjs.event;

import org.jetbrains.annotations.*;

/**
 * @author FengMing
 * @param priority 0 is normal, the smaller the number, the earlier the handler is executed
 */
public record EventListener(@NotNull IEventHandler handler, @Nullable Object extras, int priority) implements Comparable<EventListener> {
    @Override
    public int compareTo(@NotNull EventListener o) {
        return Integer.compare(this.priority, o.priority);
    }
}
