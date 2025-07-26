package me.fengming.openjs.plugin.load.condition;

import com.google.common.collect.ImmutableBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.fengming.openjs.utils.codec.CodecUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;

/**
 * @author ZZZank
 */
record SideCond(Dist dist) implements PluginLoadCondition {
    public static final Codec<SideCond> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            CodecUtils.biMapTransform(
                Codec.STRING,
                ImmutableBiMap.of("client", Dist.CLIENT, "server", Dist.DEDICATED_SERVER)
            ).fieldOf("side").forGetter(SideCond::dist)
        ).apply(builder, SideCond::new)
    );

    @Override
    public boolean test() {
        return FMLLoader.getDist() == dist;
    }

    @Override
    public PluginLoadConditionType type() {
        return PluginLoadConditionType.SIDE;
    }
}
