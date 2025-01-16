package me.fengming.openjs.registry;

import me.fengming.openjs.event.EventGroup;
import me.fengming.openjs.registry.api.TypedRegistry;
import me.fengming.openjs.script.ScriptType;

/**
 * @author FengMing
 */
public class EventGroupRegistry extends TypedRegistry<EventGroup> {
    public EventGroupRegistry(ScriptType type) {
        super("event", type);
    }

    public void register(EventGroup group) {
        register(group.getName(), group);
    }
}
