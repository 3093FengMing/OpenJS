package me.fengming.openjs.plugin.builtin;

import me.fengming.openjs.binding.builtin.PackMode;
import me.fengming.openjs.event.startup.StartupEvents;
import me.fengming.openjs.plugin.IOpenJSPlugin;
import me.fengming.openjs.binding.BindingRegistry;
import me.fengming.openjs.registry.EventGroupRegistry;
import me.fengming.openjs.registry.OpenJSRegistries;
import me.fengming.openjs.wrapper.type.TypeWrappers;
import net.minecraft.resources.ResourceLocation;

/**
 * @author FengMing
 */
public class OpenJSBuiltinPlugin implements IOpenJSPlugin {
    @Override
    public void load() {
        TypeWrappers.registerSimple(
            ResourceLocation.class,
            (from, to) -> ResourceLocation.tryParse(String.valueOf(from))
        );
    }

    @Override
    public void registerBinding(BindingRegistry registry) {
        // placeholder for testing
        registry.register("System", System.class);
        registry.register("PackMode", PackMode.class);

        // event
        OpenJSRegistries.EVENT_GROUPS
            .getNullable(registry.type)
            .apply((name, group) -> registry.register(group.asBinding()));
    }

    @Override
    public void registerEvent(EventGroupRegistry registry) {
        registry.register(StartupEvents.GROUP);
    }
}
