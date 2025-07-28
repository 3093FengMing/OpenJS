package me.fengming.openjs.plugin.load;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.fengming.openjs.plugin.IOpenJSPlugin;
import me.fengming.openjs.plugin.load.condition.PluginLoadCondition;

import java.util.Optional;

/**
 * @author ZZZank
 */
public record LoadablePlugin(String name, Optional<PluginLoadCondition> condition) {
    public static final Codec<LoadablePlugin> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            Codec.STRING.fieldOf("name").forGetter(LoadablePlugin::name),
            PluginLoadCondition.CODEC.optionalFieldOf("condition").forGetter(LoadablePlugin::condition)
        ).apply(builder, LoadablePlugin::new)
    );

    public Optional<IOpenJSPlugin> load() throws Exception {
        if (condition.isPresent() && !condition.get().test()) {
            return Optional.empty();
        }
        var result =  Class.forName(name)
            .asSubclass(IOpenJSPlugin.class)
            .getConstructor()
            .newInstance();
        return Optional.of(result);
    }
}
