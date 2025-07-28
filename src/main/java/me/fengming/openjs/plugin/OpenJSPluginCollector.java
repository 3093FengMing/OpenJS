package me.fengming.openjs.plugin;

/**
 * @author ZZZank
 */
public interface OpenJSPluginCollector {

    void register(IOpenJSPlugin plugin);

    default void register(IOpenJSPlugin... plugins) {
        for (var plugin : plugins) {
            register(plugin);
        }
    }
}
