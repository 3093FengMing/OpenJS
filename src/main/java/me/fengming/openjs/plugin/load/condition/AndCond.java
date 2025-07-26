package me.fengming.openjs.plugin.load.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

/**
 * @author ZZZank
 */
record AndCond(List<PluginLoadCondition> conditions) implements PluginLoadCondition {
    public static final Codec<AndCond> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            PluginLoadCondition.CODEC.listOf().fieldOf("conditions").forGetter(AndCond::conditions)
        ).apply(builder, AndCond::new)
    );

    @Override
    public boolean test() {
        return conditions.stream().allMatch(PluginLoadCondition::test);
    }

    @Override
    public PluginLoadConditionType type() {
        return PluginLoadConditionType.AND;
    }
}
