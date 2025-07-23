package me.fengming.openjs.event.startup;

import me.fengming.openjs.event.js.EventBusJS;
import me.fengming.openjs.event.js.EventGroupJS;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchKey;
import me.fengming.openjs.wrapper.BuiltinTypeWrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;

/**
 * @author FengMing
 */
public interface StartupEvents {
    EventGroupJS STARTUP_EVENTS = new EventGroupJS("StartupEvents");

    EventBusJS.Dispatch<RegisterEvent, ResourceLocation> REGISTRY = STARTUP_EVENTS.addBus(
        "registry",
        DispatchEventBus.create(
            RegisterEvent.class, DispatchKey.create(
                ResourceLocation.class,
                e -> e.getRegistryKey().location(),
                BuiltinTypeWrappers::resourceLocation
            )
        )
    );
}
