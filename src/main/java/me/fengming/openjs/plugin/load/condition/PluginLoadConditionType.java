package me.fengming.openjs.plugin.load.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import me.fengming.openjs.OpenJS;
import me.fengming.openjs.registry.api.IRegistration;
import me.fengming.openjs.registry.api.SimpleRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Function;

/**
 * @author ZZZank
 */
public record PluginLoadConditionType(Codec<? extends PluginLoadCondition> codec) implements IRegistration {
    public static final SimpleRegistry<PluginLoadConditionType> REGISTRY =
        new SimpleRegistry<>("plugin_load_condition");

    public static final PluginLoadConditionType AND = new PluginLoadConditionType(AndCond.CODEC);
    public static final PluginLoadConditionType OR = new PluginLoadConditionType(OrCond.CODEC);
    public static final PluginLoadConditionType NOT = new PluginLoadConditionType(NotCond.CODEC);
    public static final PluginLoadConditionType CONSTANT = new PluginLoadConditionType(ConstantCond.CODEC);
    public static final PluginLoadConditionType MOD = new PluginLoadConditionType(ModCond.CODEC);
    public static final PluginLoadConditionType SIDE = new PluginLoadConditionType(SideCond.CODEC);
}
