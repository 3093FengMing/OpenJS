package me.fengming.openjs;

import me.fengming.openjs.binding.PackMode;
import me.fengming.openjs.event.startup.StartupEvents;
import me.fengming.openjs.plugin.IOpenJSPlugin;
import me.fengming.openjs.registry.BindingRegistry;
import me.fengming.openjs.registry.EventGroupRegistry;
import me.fengming.openjs.registry.OpenJSRegistries;
import me.fengming.openjs.wrapper.EventGroupWrapper;

/**
 * @author FengMing
 */
public class OpenJSBuiltinPlugin implements IOpenJSPlugin {
    @Override
    public void load() {
        //registerBinding();
    }

    @Override
    public void registerBinding(BindingRegistry registry) {
        // placeholder for testing
        registry.register("PackMode", PackMode.class);

        // event
        OpenJSRegistries.EVENT_GROUPS
            .getNullable(registry.type)
            .apply((name, group) -> registry.register(name, new EventGroupWrapper(group)));
    }

    @Override
    public void registerEvent(EventGroupRegistry registry) {
        registry.register(StartupEvents.STARTUP_EVENTS);
    }

}
