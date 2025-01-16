package me.fengming.openjs.event.startup;

import me.fengming.openjs.event.OpenJSEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;

import java.util.function.Supplier;

/**
 * @author FengMing
 */
public class RegistryEvent extends OpenJSEvent {
    private final RegisterEvent event;

    public RegistryEvent(RegisterEvent event) {
        this.event = event;
    }

    public <T> void register(ResourceLocation id, Supplier<T> supplier) {
        if (event.getForgeRegistry() != null) {
            event.getForgeRegistry().register(id, supplier.get());
        } else if (event.getVanillaRegistry() != null) {
            Registry.register(event.getVanillaRegistry(), id, supplier.get());
        }
    }
}
