package me.fengming.openjs.event.startup;

import me.fengming.openjs.event.js.EventBusForgeBinding;
import me.fengming.openjs.event.js.EventBusJS;
import me.fengming.openjs.event.js.EventGroupJS;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchKey;
import me.fengming.openjs.wrapper.BuiltinTypeWrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.RegisterEvent;

/**
 * @author FengMing
 */
public interface StartupEvents {
    EventGroupJS GROUP = new EventGroupJS("StartupEvents");

    EventBusJS<RegisterEvent, ResourceLocation> REGISTRY = GROUP.createBus(
        "registry",
        RegisterEvent.class,
        false,
        DispatchKey.create(ResourceLocation.class, e -> e.getRegistryKey().location()),
        BuiltinTypeWrappers::resourceLocation
    );

    EventBusForgeBinding FORGE_BINDING = EventBusForgeBinding.create(MinecraftForge.EVENT_BUS)
        .bind(REGISTRY);
}
