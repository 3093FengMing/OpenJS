package me.fengming.openjs.registry;

import me.fengming.openjs.event.js.EventGroupJS;
import me.fengming.openjs.registry.api.TypedRegistry;
import me.fengming.openjs.script.ScriptType;

/**
 * @author FengMing
 */
public class EventGroupRegistry extends TypedRegistry<EventGroupJS> {
    public EventGroupRegistry(ScriptType type) {
        super("event", type);
    }

    public void register(EventGroupJS group) {
        register(group.name(), group);
    }
}
