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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author FengMing
 */
public class OpenJSPlugins {
    public static final List<IOpenJSPlugin> plugins = new ArrayList<>();

    static {
        MinecraftForge.EVENT_BUS.post(new PluginLoadConditionTypeRegistryEvent(PluginLoadConditionType.REGISTRY));
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
                    .map(LoadablePlugin::load)
                    .flatMap(result -> result.resultOrPartial(errorReporter))
                    .ifPresent(plugins::add);
            }
        } catch (Exception e) {
            errorReporter.accept(e.getMessage());
        }
    }
}
