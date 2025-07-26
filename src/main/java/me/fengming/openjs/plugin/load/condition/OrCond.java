package me.fengming.openjs.plugin.load.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

/**
 * @author ZZZank
 */
record OrCond(List<PluginLoadCondition> conditions) implements PluginLoadCondition {
    public static final Codec<OrCond> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            PluginLoadCondition.CODEC.listOf().fieldOf("conditions").forGetter(OrCond::conditions)
        ).apply(builder, OrCond::new)
    );

    @Override
    public boolean test() {
        return conditions.stream().anyMatch(PluginLoadCondition::test);
    }

    @Override
    public PluginLoadConditionType type() {
        return PluginLoadConditionType.OR;
    }
}
