package me.fengming.openjs.event.startup;

import me.fengming.openjs.event.js.EventBusForgeBridge;
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

    EventBusJS<RegistryEventJS, ResourceLocation> REGISTRY = GROUP.addBus(
        "registry",
        EventBusJS.of(
            RegistryEventJS.class,
            false,
            DispatchKey.create(ResourceLocation.class, RegistryEventJS::registryId),
            BuiltinTypeWrappers::resourceLocation
        )
    );

    EventBusForgeBridge FORGE_BRIDGE = EventBusForgeBridge.create(MinecraftForge.EVENT_BUS)
        .bindBridged(REGISTRY, RegistryEventJS::new, RegisterEvent.class);
}
