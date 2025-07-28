package me.fengming.openjs.plugin;

/**
 * @author ZZZank
 */
public class OpenJSBuiltinPluginLoader implements OpenJSPluginLoader {
    @Override
    public void registerPlugins(OpenJSPluginCollector collector) {
        collector.register(new OpenJSBuiltinPlugin());
    }
}
