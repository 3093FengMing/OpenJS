package me.fengming.openjs.plugin;

/**
 * @author ZZZank
 */
public interface PluginRegistryContext {

    void register(IOpenJSPlugin plugin);

    default void register(IOpenJSPlugin... plugins) {
        for (var plugin : plugins) {
            register(plugin);
        }
    }

    boolean isModLoaded(String modId);

    boolean isClient();

    boolean isServer();

    boolean isDevelopmentEnvironment();
}
