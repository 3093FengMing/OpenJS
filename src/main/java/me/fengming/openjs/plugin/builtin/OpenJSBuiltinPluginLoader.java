package me.fengming.openjs.plugin.builtin;

import me.fengming.openjs.plugin.OpenJSPluginCollector;
import me.fengming.openjs.plugin.OpenJSPluginLoader;

/**
 * @author ZZZank
 */
public class OpenJSBuiltinPluginLoader implements OpenJSPluginLoader {
    @Override
    public void registerPlugins(OpenJSPluginCollector collector) {
        collector.register(new OpenJSBuiltinPlugin());
    }
}
