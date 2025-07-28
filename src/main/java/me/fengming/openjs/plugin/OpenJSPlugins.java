package me.fengming.openjs.plugin;

import com.google.gson.JsonArray;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import me.fengming.openjs.OpenJS;
import me.fengming.openjs.plugin.load.LoadablePlugin;
import me.fengming.openjs.plugin.load.PluginLoadConditionTypeRegistryEvent;
import me.fengming.openjs.plugin.load.condition.PluginLoadConditionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.forgespi.language.IModFileInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author FengMing
 */
public class OpenJSPlugins {
    private static final List<IOpenJSPlugin> PLUGINS = new ArrayList<>();

    static {
        MinecraftForge.EVENT_BUS.post(new PluginLoadConditionTypeRegistryEvent(PluginLoadConditionType.REGISTRY));
    }

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

    public static void load(IModFileInfo info) {
        Path path = info.getFile().findResource("openjs_plugins.json");
        if (!Files.exists(path)) {
            return;
        }

        var errorReporter = (Consumer<String>) OpenJS.LOGGER::error;
        try (var reader = Files.newBufferedReader(path)) {
            var json = OpenJS.GSON.fromJson(reader, JsonArray.class);
            for (var element : json) {
                LoadablePlugin.CODEC.decode(JsonOps.INSTANCE, element)
                    .resultOrPartial(errorReporter)
                    .map(Pair::getFirst)
                    .ifPresent(loadable -> {
                        try {
                            loadable.load().ifPresent(PLUGINS::add);
                        } catch (Exception e) {
                            OpenJS.LOGGER.error("Error when loading plugin '{}'", loadable.name(), e);
                        }
                    });
            }
        } catch (Exception e) {
            errorReporter.accept(e.getMessage());
        }
    }
}
