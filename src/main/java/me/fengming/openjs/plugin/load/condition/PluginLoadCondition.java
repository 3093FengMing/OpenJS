package me.fengming.openjs.plugin.load.condition;

import com.mojang.serialization.Codec;

/**
 * @author ZZZank
 */
public interface PluginLoadCondition {
    Codec<PluginLoadCondition> CODEC = PluginLoadConditionType.REGISTRY.byNameCodec()
        .dispatch(PluginLoadCondition::type, PluginLoadConditionType::codec);

    boolean test();

    PluginLoadConditionType type();
}
