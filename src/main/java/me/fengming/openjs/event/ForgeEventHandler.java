package me.fengming.openjs.event;

import me.fengming.openjs.event.startup.StartupEvents;
import net.minecraftforge.registries.RegisterEvent;

/**
 * @author FengMing
 */
public class ForgeEventHandler {
    public static void handleRegister(RegisterEvent event) {
        StartupEvents.REGISTRY.post(event);
    }
}
