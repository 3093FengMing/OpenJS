package me.fengming.openjs.plugin.load.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * @author ZZZank
 */
enum ConstantCond implements PluginLoadCondition {
    ALWAYS,
    NEVER;

    public static final Codec<ConstantCond> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            Codec.BOOL.fieldOf("value").forGetter(ConstantCond::test)
        ).apply(builder, ConstantCond::of)
    );

    public static ConstantCond of(boolean value) {
        return value ? ALWAYS : NEVER;
    }

    @Override
    public boolean test() {
        return this == ALWAYS;
    }

    @Override
    public PluginLoadConditionType type() {
        return PluginLoadConditionType.CONSTANT;
    }
}
