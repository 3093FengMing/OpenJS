package me.fengming.openjs.plugin.load.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.fengming.openjs.utils.codec.OpenJSCodecs;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import org.apache.maven.artifact.versioning.VersionRange;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author ZZZank
 */
record ModCond(
    String id,
    Optional<Pattern> versionPattern,
    Optional<VersionRange> versionRange
) implements PluginLoadCondition {
    public static final Codec<ModCond> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            Codec.STRING.fieldOf("id").forGetter(ModCond::id),
            OpenJSCodecs.PATTERN.optionalFieldOf("version_pattern").forGetter(ModCond::versionPattern),
            OpenJSCodecs.VERSION_RANGE.optionalFieldOf("version_range").forGetter(ModCond::versionRange)
        ).apply(builder, ModCond::new)
    );

    @Override
    public boolean test() {
        var mod = ModList.get()
            .getModContainerById(id)
            .map(ModContainer::getModInfo)
            .orElse(null);
        if (mod == null) {
            return false;
        }
        if (versionRange.isPresent()) {
            return versionRange.get().containsVersion(mod.getVersion());
        } else if (versionPattern.isPresent()) {
            return versionPattern.get().matcher(mod.getVersion().toString()).matches();
        }
        return true;
    }

    @Override
    public PluginLoadConditionType type() {
        return PluginLoadConditionType.MOD;
    }
}
