package me.fengming.openjs.plugin.builtin;

import me.fengming.openjs.plugin.PluginRegistryContext;
import me.fengming.openjs.plugin.OpenJSPluginLoader;

/**
 * @author ZZZank
 */
public class OpenJSBuiltinPluginLoader implements OpenJSPluginLoader {
    @Override
    public void registerPlugins(PluginRegistryContext context) {
        context.register(new OpenJSBuiltinPlugin());
    }
}
