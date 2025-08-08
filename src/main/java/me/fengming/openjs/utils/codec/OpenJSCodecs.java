package me.fengming.openjs.utils.codec;

import com.mojang.serialization.Codec;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.apache.maven.artifact.versioning.VersionRange;

import java.util.regex.Pattern;

/**
 * @author ZZZank
 */
public interface OpenJSCodecs {
    Codec<Pattern> PATTERN =
        Codec.STRING.comapFlatMap(CodecUtils.unsafeFn(Pattern::compile), Pattern::pattern);
    Codec<ComparableVersion> COMPARABLE_VERSION =
        Codec.STRING.xmap(ComparableVersion::new, ComparableVersion::toString);
    Codec<VersionRange> VERSION_RANGE =
        Codec.STRING.comapFlatMap(CodecUtils.unsafeFn(VersionRange::createFromVersionSpec), VersionRange::toString);
}
