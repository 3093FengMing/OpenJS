package me.fengming.openjs.registry;

import me.fengming.openjs.event.EventGroup;

/**
 * @author FengMing
 */
public class EventGroupRegistry extends SimpleRegistry<EventGroup> {
    public EventGroupRegistry() {
        super("event");
    }

    public void register(EventGroup group) {
        register(group.getName(), group);
    }

    @Override
    public EventGroup register(String key, EventGroup event) {
        return super.register(key, event);
    }
}
