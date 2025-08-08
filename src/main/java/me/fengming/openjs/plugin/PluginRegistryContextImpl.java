package me.fengming.openjs.plugin;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

/**
 * @author ZZZank
 */
final class PluginRegistryContextImpl implements PluginRegistryContext {
    @Override
    public void register(IOpenJSPlugin plugin) {
        OpenJSPlugins.register(plugin);
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isClient() {
        return FMLLoader.getDist().isClient();
    }

    @Override
    public boolean isServer() {
        return FMLLoader.getDist().isDedicatedServer();
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}
