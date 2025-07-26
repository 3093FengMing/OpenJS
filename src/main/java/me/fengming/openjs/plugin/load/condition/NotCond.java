package me.fengming.openjs.plugin.load.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * @author ZZZank
 */
record NotCond(PluginLoadCondition condition) implements PluginLoadCondition {
    public static final Codec<NotCond> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            PluginLoadCondition.CODEC.fieldOf("condition").forGetter(NotCond::condition)
        ).apply(builder, NotCond::new)
    );

    @Override
    public boolean test() {
        return !condition.test();
    }

    @Override
    public PluginLoadConditionType type() {
        return PluginLoadConditionType.NOT;
    }
}
