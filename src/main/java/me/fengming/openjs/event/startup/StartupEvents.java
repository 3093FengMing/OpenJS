package me.fengming.openjs.event.startup;

import me.fengming.openjs.event.EventGroup;
import me.fengming.openjs.event.EventHandler;
import me.fengming.openjs.script.ScriptType;

/**
 * @author FengMing
 */
public interface StartupEvents {
    EventGroup STARTUP_EVENTS = EventGroup.create("StartupEvents");

    EventHandler<RegistryEvent> REGISTRY = STARTUP_EVENTS.add("registry", ScriptType.STARTUP);
}
