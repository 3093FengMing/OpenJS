package me.fengming.openjs.event.startup;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;

import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public record RegistryEventJS(RegisterEvent forgeEvent) {

    public ResourceLocation registryId() {
        return forgeEvent.getRegistryKey().location();
    }

    public void register(ResourceLocation name, Supplier<? extends Registry<?>> provider) {
        forgeEvent.register((ResourceKey) forgeEvent.getRegistryKey(), name, provider);
    }
}
