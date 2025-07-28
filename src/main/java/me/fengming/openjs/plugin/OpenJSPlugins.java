package me.fengming.openjs.plugin;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author FengMing
 */
public class OpenJSPlugins {
    private static final List<IOpenJSPlugin> PLUGINS = new ArrayList<>();

    public static void register(IOpenJSPlugin plugin) {
        PLUGINS.add(Objects.requireNonNull(plugin));
    }

    public static List<IOpenJSPlugin> view() {
        return Collections.unmodifiableList(PLUGINS);
    }

    public static void forEach(Consumer<? super IOpenJSPlugin> action) {
        PLUGINS.forEach(action);
    }

    public static void loadFromServices() {
        for (var pluginLoader : ServiceLoader.load(OpenJSPluginLoader.class)) {
            pluginLoader.registerPlugins(OpenJSPlugins::register);
        }
    }
}
