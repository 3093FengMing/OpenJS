package me.fengming.openjs.event;

import me.fengming.openjs.event.startup.RegistryEvent;
import me.fengming.openjs.event.startup.StartupEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;

/**
 * @author FengMing
 */
public class ForgeEventHandler {
    public static void handleRegister(RegisterEvent event) {
        ResourceLocation rl = event.getRegistryKey().location();
        if ("minecraft".equals(rl.getNamespace())) {
            StartupEvents.REGISTRY.post(new RegistryEvent(event), rl.getPath());
        }
        StartupEvents.REGISTRY.post(new RegistryEvent(event), rl.toString());
    }
}
