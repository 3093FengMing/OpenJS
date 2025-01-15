package me.fengming.openjs.event;

import me.fengming.openjs.registry.api.IRegistration;
import me.fengming.openjs.script.ScriptType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FengMing
 */
public class EventGroup implements IRegistration {
    private final Map<String, EventHandler<?>> handlers = new HashMap<>();
    private final String name;

    private EventGroup(String name) {
        this.name = name;
    }

    public static EventGroup create(String name) {
        return new EventGroup(name);
    }

    public String getName() {
        return this.name;
    }

    public <T extends OpenJSEvent> EventHandler<T> add(String name, ScriptType type) {
        var handler = new EventHandler<T>(name, type);
        handlers.put(name, handler);
        return handler;
    }

    public Map<String, EventHandler<?>> getHandlers() {
        return this.handlers;
    }
}
