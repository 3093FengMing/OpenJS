package me.fengming.openjs.wrapper;

import me.fengming.openjs.event.EventGroup;
import org.mozilla.javascript.BaseFunction;

import java.util.HashMap;

/**
 * @author FengMing
 */
public class EventGroupWrapper extends HashMap<String, BaseFunction> {
    private final EventGroup group;

    public EventGroupWrapper(EventGroup group) {
        this.group = group;
    }

    /**
     * Equivalent to property calling. It will return a function.
     * @param name the name of the group
     * @return a function as event handler
     */
    @Override
    public BaseFunction get(Object name) {
        return this.group.getHandlers().get(String.valueOf(name));
    }

    @Override
    public boolean containsKey(Object key) {
        return true;
    }
}
